package com.example.aranoz.services.order;

import com.example.aranoz.domain.dtoS.model.Order.OrderCheckoutFormDto;
import com.example.aranoz.domain.dtoS.view.Order.PaymentVM;
import com.example.aranoz.domain.dtoS.view.User.CartProductVM;
import com.example.aranoz.domain.entities.Order;
import com.example.aranoz.repository.OrderRepository;
import com.example.aranoz.services.email.EmailService;
import com.example.aranoz.services.orderPaymentMethod.OrderPaymentMethodService;
import com.example.aranoz.services.orderStatus.OrderStatusService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Date;
import java.util.List;

import static com.example.aranoz.constants.Constants.CARD_PAYMENT;
import static com.example.aranoz.constants.Constants.CASH_PAYMENT;

@Service
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final EmailService emailService;
    private final OrderPaymentMethodService orderPaymentMethodService;
    private final OrderStatusService orderStatusService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, EmailService emailService, OrderPaymentMethodService orderPaymentMethodService, OrderStatusService orderStatusService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.orderRepository = orderRepository;
        this.emailService = emailService;
        this.orderPaymentMethodService = orderPaymentMethodService;
        this.orderStatusService = orderStatusService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public Order createOrder(OrderCheckoutFormDto model, List<CartProductVM> cookieProducts) throws JsonProcessingException {
        Order order = modelMapper.map(model,Order.class);
        order.setProductAndQuantities(objectMapper.writeValueAsString(cookieProducts));

        return order;
    }

    @Override
    public void saveOrder(PaymentVM model, String userId) throws MessagingException, JsonProcessingException {
        switch (model.getMethod()) {
            case CASH_PAYMENT -> model.getOrder().setPaymentMethod(this.orderPaymentMethodService.getCashPayment());
            case CARD_PAYMENT -> model.getOrder().setPaymentMethod(this.orderPaymentMethodService.getCardPayment());
        }

        model.getOrder().setUserId(userId);
        model.getOrder().setStatus(this.orderStatusService.getAcceptedStatus());
        model.getOrder().setDateOfCreation(new Date());

        this.orderRepository.save(model.getOrder());

        emailService.sendAcceptedOrderEmail(model.getOrder().getEmail(),
                                            getOrderProductsCount(model.getOrder().getProductAndQuantities()),
                                            model.getOrder().getTotal(),
                                            findByEmailAndDate(model.getOrder().getEmail(),model.getOrder().getDateOfCreation()).getId());
    }

    private Long getOrderProductsCount(String productAndQuantities) throws JsonProcessingException {
        int count = 0;
        List<CartProductVM> orderProducts = objectMapper.readValue(productAndQuantities, new TypeReference<List<CartProductVM>>(){});
        for (CartProductVM orderProduct : orderProducts) {
            count+=orderProduct.getQuantity();
        }
        return (long) count;
    }

    @Override
    public Order findById(String id) {
        return this.orderRepository.findById(id).get();
    }

    @Override
    public Order findByEmailAndDate(String email, Date dateOfCreation) {
        return this.orderRepository.findByEmailAndDateOfCreation(email,dateOfCreation);
    }

    @Override
    public List<CartProductVM> getProductsAndQuantities(String productAndQuantities) throws JsonProcessingException {
        return this.objectMapper
                .readValue(productAndQuantities, new TypeReference<List<CartProductVM>>(){});
    }

    @Override
    public Cookie[] updateCookiesForDelete(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (!cookie.getName().contains("user") && !cookie.getName().contains("AspNet") && !cookie.getName().contains("idea")){
                cookie.setMaxAge(0);
            }
        }
        return cookies;
    }

    @Override
    public void removeCookieById( Cookie[] cookies , String id) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(id)){
                cookie.setMaxAge(0);
            }
        }
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        return orderRepository.getOrdersByUserId(userId);
    }
}