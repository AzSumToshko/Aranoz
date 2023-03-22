package com.example.aranoz.services.productCategory;

import com.example.aranoz.domain.entities.ProductCategory;
import com.example.aranoz.domain.enums.Category;
import com.example.aranoz.repository.ProductCategoryRepository;
import com.example.aranoz.services.init.DataBaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService, DataBaseInitService {
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public void dbInit() {
        List<ProductCategory> categories = new ArrayList<>();
        categories.add(new ProductCategory().setCategory(Category.BED));
        categories.add(new ProductCategory().setCategory(Category.CABINET));
        categories.add(new ProductCategory().setCategory(Category.SOFA));
        categories.add(new ProductCategory().setCategory(Category.CHAIR));
        categories.add(new ProductCategory().setCategory(Category.TABLE));

        this.productCategoryRepository.saveAllAndFlush(categories);
    }

    @Override
    public boolean isDbInit() {
        return this.productCategoryRepository.count() > 0;
    }

    @Override
    public List<ProductCategory> getAll() {
        return productCategoryRepository.findAll();
    }

    @Override
    public ProductCategory findByCategory(Category category) {
        return this.productCategoryRepository.findFirstByCategory(category);
    }
}
