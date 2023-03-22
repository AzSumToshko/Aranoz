package com.example.aranoz.domain.entities;

import com.example.aranoz.domain.enums.Category;
import com.example.aranoz.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private Category category;

    public ProductCategory setCategory(Category category) {
        this.category = category;
        return this;
    }
}
