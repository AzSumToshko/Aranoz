package com.example.aranoz.services.order;

import com.example.aranoz.domain.dtoS.model.Order.OrderCheckoutFormDto;
import com.example.aranoz.domain.dtoS.view.Order.PaymentVM;
import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

public interface OrderService{
    Order createOrder(OrderCheckoutFormDto model, List<CartProductVM> cookieProducts) throws JsonProcessingException;
    void saveOrder(PaymentVM model, String userId) throws MessagingException, JsonProcessingException;
    Order findById(String id);
    Order findByEmailAndDate(String email, Date dateOfCreation);
    List<CartProductVM> getProductsAndQuantities(String productAndQuantities) throws JsonProcessingException;
    Cookie[] updateCookiesForDelete(Cookie[] cookies);
    void removeCookieById(Cookie[] cookies,String id);
    List<Order> getUserOrders(String userId);
}
