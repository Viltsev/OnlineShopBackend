package com.online_shop.onlineShop.Products.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;

import java.util.List;

@Data
@Entity
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleSize;
    private List<String> size;
}
