package com.example.aranoz.services.productCategory;

import com.example.aranoz.domain.entities.ProductCategory;
import com.example.aranoz.domain.enums.Category;
import com.example.aranoz.services.init.DataBaseInitService;

import java.util.List;

public interface ProductCategoryService extends DataBaseInitService {
    List<ProductCategory> getAll();
    ProductCategory findByCategory(Category name);
}
