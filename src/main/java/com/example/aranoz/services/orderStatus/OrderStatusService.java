package com.example.aranoz.services.orderStatus;

import com.example.aranoz.domain.entities.OrderStatus;
import com.example.aranoz.services.init.DataBaseInitService;

public interface OrderStatusService extends DataBaseInitService {
    OrderStatus getAcceptedStatus();
}
