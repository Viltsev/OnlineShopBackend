package com.online_shop.onlineShop.Scraper;

import jakarta.persistence.GeneratedValue;
import lombok.Data;

import java.util.List;

@Data
public class Product {
    @GeneratedValue
    private Long id;
    private String title;
    private String price;
    private List<String> imageUrls;
}
