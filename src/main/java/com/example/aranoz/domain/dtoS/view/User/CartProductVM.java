package com.example.aranoz.domain.dtoS.view.User;

import com.example.aranoz.domain.entities.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartProductVM {
    private Product product;
    private Long quantity;
    private Double total;
}
