package com.example.aranoz.services.cartItem;

import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.CartItem;
import com.example.aranoz.domain.entities.Product;

import java.util.List;

public interface CartItemService {
    void removeCartItemByProductId(String id , String userId);

    List<CartProductVM> getCartItemsForLoggedUser(String id);
    List<CartItem> getCartItems(String id);
    void addToCart(CartItem item);
}
