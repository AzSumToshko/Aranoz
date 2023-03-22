package com.example.aranoz.domain.entities;

import com.example.aranoz.util.ImageUtil;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "products")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity{

    @Column(unique = true)
    private String name;

    @Column(nullable = false)
    private Double price;

    @OneToOne
    private ProductCategory category;

    @Column(nullable = false)
    private String description;

    @Lob
    @Column(name = "image", columnDefinition = "mediumblob" , nullable = false)
    private byte[] image;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer depth;

    @Column(nullable = false)
    private Integer weight;

    @Column(nullable = false)
    private String qualityChecking;

    @Column(nullable = false)
    private Integer peaces;

    public Product(String name, String description, double price) {
        setName(name);
        setDescription(description);
        setPrice(price);
    }

    public Product(String id) {
        setId(id);
    }

    public String convertImageToString() {
        return ImageUtil.getImgData(image);
    }
}
