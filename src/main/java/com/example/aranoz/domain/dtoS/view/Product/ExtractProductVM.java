package com.example.aranoz.domain.dtoS.view.Product;

import com.example.aranoz.domain.entities.ProductCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExtractProductVM {
    private String name;

    private Double price;

    private ProductCategory category;

    private String description;

    private String image;

    private Integer width;

    private Integer height;

    private Integer depth;

    private Integer weight;

    private String qualityChecking;

    private Integer peaces;
}
