package com.example.aranoz.services.cartItem;

import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.CartItem;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.repository.CartItemRepository;
import com.example.aranoz.repository.ProductRepository;
import com.example.aranoz.services.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService{
    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;
    @Autowired
    public CartItemServiceImpl(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;

    }

    @Override
    public void removeCartItemByProductId(String id , String userId) {
        this.cartItemRepository.removeByProductIdAndUserId(id,userId);
    }

    @Override
    public List<CartProductVM> getCartItemsForLoggedUser(String id) {
        List<CartProductVM> products = new ArrayList<>();

        for (CartItem cartItem : this.cartItemRepository.getCartItemsByUserId(id)) {
            Product product = this.productRepository.findById(cartItem.getProductId()).get();
            products.add(CartProductVM.builder().product(product).quantity(cartItem.getQuantity()).total(product.getPrice()*cartItem.getQuantity()).build());
        }

        return products;
    }

    @Override
    public List<CartItem> getCartItems(String id) {
        return this.cartItemRepository.getCartItemsByUserId(id);
    }

    @Override
    public void addToCart(CartItem item) {
        cartItemRepository.saveAndFlush(item);
    }


}
