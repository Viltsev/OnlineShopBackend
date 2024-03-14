package com.online_shop.onlineShop.Products.repo;

import com.online_shop.onlineShop.Products.model.ShoesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoesCategoryRepository extends JpaRepository<ShoesCategory, Long> {
}
