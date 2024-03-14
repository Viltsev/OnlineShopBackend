package com.online_shop.onlineShop.Products.model;

import com.online_shop.onlineShop.Products.model.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
@Table(name = "category_shoes")
public class ShoesCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoryTitle;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private List<Product> productList;
}
