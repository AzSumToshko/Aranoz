package com.example.aranoz.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cart_items")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItem extends BaseEntity{

    private String userId;
    private String productId;

    private Long quantity;

    public CartItem(String id, String userId, Long quantity, String productId) {
        setId(id);
        setUserId(userId);
        setQuantity(quantity);
        setProductId(productId);
    }
}
