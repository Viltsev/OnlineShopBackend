package com.online_shop.onlineShop.Cart.controllers;

import com.online_shop.onlineShop.Cart.model.CartReq;
import com.online_shop.onlineShop.Cart.services.CartService;
import com.online_shop.onlineShop.Products.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {
    private final CartService service;
    @PostMapping("/save_item")
    public String createData(@RequestBody CartReq cartReq) throws IOException {
        return service.saveToCart(cartReq.getId(), cartReq.getEmail());
    }

    @GetMapping("/get_cart/{email}")
    public List<Product> getCartItems(@PathVariable String email) {
        return service.getCartItems(email);
    }
}
