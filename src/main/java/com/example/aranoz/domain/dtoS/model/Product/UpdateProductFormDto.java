package com.example.aranoz.domain.dtoS.model.Product;

import com.example.aranoz.domain.enums.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class UpdateProductFormDto {

    @NotNull
    private String id;

    @NotNull
    @Size(min = 5, max = 50)
    private String name;

    @NotNull
    private Double price;

    @NotNull
    private Category category;

    @NotNull
    @Size(min = 5, max = 100)
    private String description;

    @NotNull
    private MultipartFile image;

    @NotNull
    private Integer width;

    @NotNull
    private Integer height;

    @NotNull
    private Integer depth;

    @NotNull
    private Integer weight;

    @NotNull
    @Size(min = 2, max = 25)
    private String qualityChecking;

    @NotNull
    private Integer peaces;
}
