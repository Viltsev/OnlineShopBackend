package com.online_shop.onlineShop.Scraper;
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

@Service
@RequiredArgsConstructor
public class ScraperService {
    private static final Logger logger = LoggerFactory.getLogger(ScraperService.class);

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

            Element titleElement = li.selectFirst("a.products__item-title");
            Element priceElement = li.selectFirst("p.products__item-price");

            Element itemGalleryLink = page.selectFirst("a.products__item-gallery");

            if (itemGalleryLink != null) {
                String itemUrl = itemGalleryLink.attr("href");
                List<String> newImages = scrapeImages(itemUrl);
                imageUrls.addAll(newImages);
            }

            if (titleElement != null && priceElement != null) {
                String productTitle = titleElement.text();
                String productPrice = priceElement.text();
                System.out.println("Product title " + productTitle);
                System.out.println("Product price " + productPrice);
                imageUrls.forEach(System.out::println);
            }
        }

        return productList;
    }

    public String scrapeData() throws IOException {
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

            ShoesCategory shoesCategory = ShoesCategory
                    .builder()
                    .categoryTitle(entry.getValue())
                    .productList(productList)
                    .build();

            shoesCategories.add(shoesCategory);
        }

        return "Data has been successfully scrapped!";
    }
}