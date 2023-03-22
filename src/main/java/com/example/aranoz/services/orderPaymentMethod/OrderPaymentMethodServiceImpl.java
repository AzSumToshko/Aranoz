package com.example.aranoz.services.orderPaymentMethod;

import com.example.aranoz.domain.entities.PaymentMethod;
import com.example.aranoz.domain.enums.Method;
import com.example.aranoz.repository.OrderPaymentMethodRepository;
import com.example.aranoz.services.init.DataBaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderPaymentMethodServiceImpl implements OrderPaymentMethodService, DataBaseInitService {
    private final OrderPaymentMethodRepository orderPaymentMethodRepository;

    @Autowired
    public OrderPaymentMethodServiceImpl(OrderPaymentMethodRepository orderPaymentMethodRepository) {
        this.orderPaymentMethodRepository = orderPaymentMethodRepository;
    }

    @Override
    public void dbInit() {
        List<PaymentMethod> payments = new ArrayList<>();

        payments.add(new PaymentMethod().setMethod(Method.CASH));
        payments.add(new PaymentMethod().setMethod(Method.CARD));

        this.orderPaymentMethodRepository.saveAllAndFlush(payments);
    }

    @Override
    public boolean isDbInit() {
        return this.orderPaymentMethodRepository.count() > 0;
    }

    @Override
    public PaymentMethod getCashPayment() {
        return this.orderPaymentMethodRepository.findByMethod(Method.CASH);
    }

    @Override
    public PaymentMethod getCardPayment() {
        return this.orderPaymentMethodRepository.findByMethod(Method.CARD);
    }
}
