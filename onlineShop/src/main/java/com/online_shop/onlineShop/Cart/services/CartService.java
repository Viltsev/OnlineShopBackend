package com.online_shop.onlineShop.Cart.services;

import com.online_shop.onlineShop.Products.model.Product;

import java.util.List;
import java.util.Optional;

public interface CartService {
    String saveToCart(Long id, String email);
    List<Product> getCartItems(String email);
    Optional<Product> getProductById(Long id);
}
