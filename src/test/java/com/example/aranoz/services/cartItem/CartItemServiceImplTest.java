package com.example.aranoz.services.cartItem;

import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.CartItem;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.repository.CartItemRepository;
import com.example.aranoz.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CartItemServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private CartItemRepository cartItemRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private CartItemService cartItemService;

    @BeforeEach
    void setUp() {
        Product product1 = new Product("product1", "Product 1", 10.0);
        Product product2 = new Product("product2", "Product 2", 20.0);
        entityManager.persist(product1);
        entityManager.persist(product2);

        String userId = "123";
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("1", product1.getId(), 2L, userId));
        cartItems.add(new CartItem("2", product2.getId(), 1L, userId));
        when(cartItemRepository.getCartItemsByUserId(userId)).thenReturn(cartItems);
        when(cartItemRepository.findByProductIdAndUserId("1", userId)).thenReturn(cartItems.get(0));
    }

    @Test
    public void testRemoveCartItemByProductId() {
        String productId = "1";
        String userId = "123";
        cartItemService.removeCartItemByProductId(productId, userId);
        verify(cartItemRepository, times(1)).removeByProductIdAndUserId(productId, userId);
    }

    @Test
    public void testGetCartItemsForLoggedUser() {
        String userId = "123";

        List<CartProductVM> cartProductVMs = cartItemService.getCartItemsForLoggedUser(userId);
        assertEquals(2, cartProductVMs.size());
        assertEquals("product1", cartProductVMs.get(0).getProduct().getId());
        assertEquals(2, cartProductVMs.get(0).getQuantity());
        assertEquals(20.0, cartProductVMs.get(0).getTotal());
        assertEquals("product2", cartProductVMs.get(1).getProduct().getId());
        assertEquals(1, cartProductVMs.get(1).getQuantity());
        assertEquals(20.0, cartProductVMs.get(1).getTotal());
    }

    @Test
    public void testGetCartItems() {
        String userId = "123";

        List<CartItem> result = cartItemService.getCartItems(userId);
        assertEquals(2, result.size());
    }

    @Test
    public void testAddToCart() {
        CartItem item = new CartItem("3", "product2", 2L, "123");
        when(productRepository.findById("product2")).thenReturn(Optional.of(new Product("product2", "Product 2", 20.0)));
        cartItemService.addToCart(item);
        verify(cartItemRepository, times(1)).saveAndFlush(item);
    }
}