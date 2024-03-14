package com.online_shop.onlineShop.Scraper;

import jakarta.persistence.GeneratedValue;
import lombok.*;

import java.util.List;

@Data
@Builder
public class ShoesCategory {
    @GeneratedValue
    private Long id;
    private String categoryTitle;
    private List<Product> productList;
}
