package com.online_shop.onlineShop.Products.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String price;
    private List<String> imageUrls;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "size_id")
    private List<Size> sizeList;
}
