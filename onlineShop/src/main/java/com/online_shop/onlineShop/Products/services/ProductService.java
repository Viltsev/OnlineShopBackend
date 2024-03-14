package com.online_shop.onlineShop.Products.services;

import com.online_shop.onlineShop.Products.model.ShoesCategory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    String createCategory() throws IOException;
    List<ShoesCategory> getAllShoesCategories();
}
