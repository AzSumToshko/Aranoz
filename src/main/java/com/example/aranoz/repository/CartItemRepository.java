package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.CartItem;
import com.example.aranoz.domain.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    @Transactional
    void removeByProductIdAndUserId(String productId , String userId);

    List<CartItem> getCartItemsByUserId(String id);

    CartItem findByProductIdAndUserId(String productId , String userId);
}
