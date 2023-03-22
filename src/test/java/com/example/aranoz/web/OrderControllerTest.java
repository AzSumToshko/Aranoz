package com.example.aranoz.web;

import com.example.aranoz.domain.dtoS.model.Order.OrderCheckoutFormDto;
import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.Product;
import com.example.aranoz.services.cartItem.CartItemService;
import com.example.aranoz.services.order.OrderService;
import com.example.aranoz.services.product.ProductService;
import com.example.aranoz.services.user.UserService;
import jakarta.servlet.http.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testGetCheckout() throws Exception {
        Cookie[] cookies = new Cookie[]{new Cookie("cart", "1-2-3")};
        doNothing().when(productService.getCookieProducts(cookies));
        when(productService.getTotalPriceOfCartProducts(anyList())).thenReturn(100.0);

        mockMvc.perform(get("/Order/checkout").cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(view().name("Order/checkout"))
                .andExpect(model().attribute("items", hasSize(3)))
                .andExpect(model().attribute("isAccepted", false))
                .andExpect(model().attribute("subtotal", 100.0))
                .andExpect(model().attribute("total", 105.0))
                .andExpect(model().attribute("cookies", "1-2-3"));
    }

    @Test
    public void testPostCheckoutWithErrors() throws Exception {
        OrderCheckoutFormDto formDto = new OrderCheckoutFormDto();
        formDto.setStringCookies("1-2-3");

        mockMvc.perform(post("/Order/checkout").flashAttr("orderCheckoutFormDto", formDto).param("paymentMethod", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("Order/checkout"))
                .andExpect(model().attribute("orderCheckoutFormDto", formDto))
                .andExpect(model().attribute("cookies", "1-2-3"));
    }

    @Test
    public void testPostCheckoutWithoutErrors() throws Exception {
        OrderCheckoutFormDto formDto = new OrderCheckoutFormDto();
        formDto.setStringCookies("1-2-3");

        List<CartProductVM> items = new ArrayList<>();
        when(productService.convertStringToCartProductList("1-2-3")).thenReturn(items);
        when(productService.getTotalPriceOfCartProducts(items)).thenReturn(100.0);

        mockMvc.perform(post("/Order/checkout").flashAttr("orderCheckoutFormDto", formDto).param("paymentMethod", "paypal"))
                .andExpect(status().isOk())
                .andExpect(view().name("Order/payment"))
                .andExpect(model().attribute("items", items))
                .andExpect(model().attribute("subtotal", 100.0))
                .andExpect(model().attribute("total", 105.0))
                .andExpect(model().attribute("createdOrder", notNullValue()));
    }
}