package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.domain.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Product findFirstByName(String name);
    List<Product> findAllByCategory(ProductCategory category);
    List<Product> findAllByCategoryAndPriceBetween(ProductCategory category,Double min, Double max);
    List<Product> findAllByNameContains(String category);
    List<Product> findAllByNameContainsAndPriceBetween(String category, Double min, Double max);
    List<Product> findAllByPriceBetween(Double min, Double max);
}
