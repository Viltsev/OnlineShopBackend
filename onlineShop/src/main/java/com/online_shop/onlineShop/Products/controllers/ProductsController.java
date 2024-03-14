package com.online_shop.onlineShop.Products.controllers;

import com.online_shop.onlineShop.Products.model.ShoesCategory;
import com.online_shop.onlineShop.Products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/data")
public class ProductsController {
    private final ProductService service;
    @PostMapping("/create_category")
    public String createData() throws IOException {
        return service.createCategory();
    }

    @GetMapping("/get_categories")
    public List<ShoesCategory> getData() {
        return service.getAllShoesCategories();
    }

}
