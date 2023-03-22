package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.ProductCategory;
import com.example.aranoz.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, String> {
    ProductCategory findFirstByCategory(Category category);
}
