package com.online_shop.onlineShop.Products.services;

import com.online_shop.onlineShop.Products.model.Product;
import com.online_shop.onlineShop.Products.model.ShoesCategory;
import com.online_shop.onlineShop.Products.repo.ProductRepository;
import com.online_shop.onlineShop.Products.repo.ShoesCategoryRepository;
import com.online_shop.onlineShop.Products.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {
    private final ShoesCategoryRepository repository;
    private final Scraper service;

    public String createCategory() throws IOException {
        List<ShoesCategory> shoesCategories = service.scrapeData();
        shoesCategories.forEach(repository::save);
        return "Data has been saved!";
    }

    public List<ShoesCategory> getAllShoesCategories() {
        return repository.findAll();
    }
}
