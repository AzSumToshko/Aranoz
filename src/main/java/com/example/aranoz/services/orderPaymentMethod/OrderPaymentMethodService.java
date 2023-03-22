package com.example.aranoz.services.orderPaymentMethod;

import com.example.aranoz.domain.entities.PaymentMethod;
import com.example.aranoz.services.init.DataBaseInitService;

public interface OrderPaymentMethodService extends DataBaseInitService {
    PaymentMethod getCashPayment();
    PaymentMethod getCardPayment();
}
