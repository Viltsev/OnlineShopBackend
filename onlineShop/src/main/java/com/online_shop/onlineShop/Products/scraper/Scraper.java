package com.online_shop.onlineShop.Products.scraper;
import com.online_shop.onlineShop.Products.model.Product;
import com.online_shop.onlineShop.Products.model.ShoesCategory;
import com.online_shop.onlineShop.Products.model.Size;
import com.online_shop.onlineShop.Products.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class Scraper {
    private static final Logger logger = LoggerFactory.getLogger(Scraper.class);

    public List<String> pagesScraper(String mainLink) throws IOException {
        List<String> pagesLinks = new ArrayList<>();

        pagesLinks.add(mainLink);
        Document doc = Jsoup.connect(mainLink).get();
        Elements listItems = doc.select("ul.pagination li");

        for (Element li : listItems) {
            Element link = li.selectFirst("a");
            if (link != null) {
                String href = link.attr("href");
                String text = link.text();
                if (!text.contains(">")) {
                    pagesLinks.add(href);
                }
            }
        }

        return pagesLinks;
    }

    private List<Size> scrapeSizes(String link) throws IOException {
        List<Size> sizeList = new ArrayList<>();
        List<String> usSize = new ArrayList<>();
        List<String> ukSize = new ArrayList<>();
        List<String> euSize = new ArrayList<>();
        List<String> ruSize = new ArrayList<>();

        Document page = Jsoup.connect(link).get();
        Elements sizesItems = page.getElementsByClass("ui-check__option");

        // sizes scrapping
        sizesItems.forEach(item -> {
            String size = item.text();
            String titleSize = getSizeTitle(size);
            switch (titleSize) {
                case "US" -> usSize.add(size);
                case "UK" -> ukSize.add(size);
                case "EU" -> euSize.add(size);
                case "RU" -> ruSize.add(size);
            }
        });

        Size usSizes = new Size();
        usSizes.setTitleSize("US");
        usSizes.setSize(usSize);
        Size ukSizes = new Size();
        ukSizes.setTitleSize("UK");
        ukSizes.setSize(ukSize);
        Size euSizes = new Size();
        euSizes.setTitleSize("EU");
        euSizes.setSize(euSize);
        Size ruSizes = new Size();
        ruSizes.setTitleSize("RU");
        ruSizes.setSize(ruSize);

        sizeList.add(usSizes);
        sizeList.add(ukSizes);
        sizeList.add(euSizes);
        sizeList.add(ruSizes);

        return sizeList;
    }

    private String scrapeDescription(String link) throws IOException {
        Document page = Jsoup.connect(link).get();
        Element descriptionItem = page.selectFirst("div.editor");
        return descriptionItem.text();
    }

    private List<String> scrapeImages(String link) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        Document page = Jsoup.connect(link).get();

        Elements listItems = page.select("div.sku__thumb");
        listItems.forEach(listItem -> {
            Element imageItem = listItem.selectFirst("img");
            if (imageItem != null) {
                String imageUrl = imageItem.attr("src");
                imageUrls.add(imageUrl.replace("-100x100.jpg", "-1000x1000.jpg"));
            }
        });

        return imageUrls;
    }

    public List<Product> contentScraper(String link) throws IOException {
        List<Product> productList = new ArrayList<>();

        Document page = Jsoup.connect(link).get();

        Elements listItems = page.select("ul.products__list li");

        for (Element li : listItems) {
            List<String> imageUrls = new ArrayList<>();
            List<Size> sizeList = new ArrayList<>();

            String price = "";
            String title = "";
            String description = "";

            Element titleElement = li.selectFirst("a.products__item-title");
            Element priceElement = li.selectFirst("p.products__item-price");

            if (titleElement != null && priceElement != null) {
                title = titleElement.text();
                price = normalizePrice(priceElement.text());
            }

            Element itemGalleryLink = page.selectFirst("a.products__item-gallery");

            if (itemGalleryLink != null) {
                String itemUrl = itemGalleryLink.attr("href");
                List<String> newImages = scrapeImages(itemUrl);
                List<Size> newSizes = scrapeSizes(itemUrl);
                description = scrapeDescription(itemUrl);
                imageUrls.addAll(newImages);
                sizeList.addAll(newSizes);
            }

            Product product = Product
                    .builder()
                    .price(price)
                    .title(title)
                    .description(description)
                    .imageUrls(imageUrls)
                    .sizeList(sizeList)
                    .build();

            productList.add(product);
        }

        System.out.println(productList);
        return productList;
    }

    public List<ShoesCategory> scrapeData() throws IOException {
        List<ShoesCategory> shoesCategories = new ArrayList<>();
        String mainUrl = "https://sergeev-store.ru/obuv";

        Document doc = Jsoup
                .connect(mainUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36")
                .header("Accept-Language", "*")
                .get();

        Elements categoriesElementsClass = doc.getElementsByClass("products-categories__grid products-categories__grid-4");

        Map<String, String> linksAndTitles = new HashMap<>();

        for (Element li : categoriesElementsClass.select("li")) {
            String link = li.select("a").attr("href");
            String title = li.select("span.products-categories__item-title").text();
            linksAndTitles.put(link, title);
        }

        for (Map.Entry<String, String> entry : linksAndTitles.entrySet()) {
            List<Product> productList = new ArrayList<>();
            List<String> pagesLinks = pagesScraper(entry.getKey());

            pagesLinks.forEach(link -> {
                try {
                productList.addAll(contentScraper(link));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            ShoesCategory shoesCategory = new ShoesCategory();

            shoesCategory.setCategoryTitle(entry.getValue());
            shoesCategory.setProductList(productList);

            shoesCategories.add(shoesCategory);
        }

        return shoesCategories;
    }

    private static Long generateRandomNonNegativeId() {
        return ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);
    }

    private static String normalizePrice(String price) {
        Pattern pattern = Pattern.compile("\\d+ ₽");
        Matcher matcher = pattern.matcher(price);

        String newPrice = "";

        while (matcher.find()) {
            String match = matcher.group();
            newPrice = match.replaceAll("[^\\d]", "");
            if (!newPrice.isEmpty()) {
                break;
            }
        }
        return newPrice + " ₽";
    }

    private static String getSizeTitle(String size) {
        String pattern = "(\\d+)\\s+(\\w+)";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(size);

        if (m.find()) {
            return m.group(2);
        } else {
            return "";
        }
    }
}