package com.example.aranoz.services.orderStatus;

import com.example.aranoz.domain.entities.OrderStatus;
import com.example.aranoz.domain.enums.Status;
import com.example.aranoz.repository.OrderStatusRepository;
import com.example.aranoz.services.init.DataBaseInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderStatusServiceImpl implements OrderStatusService, DataBaseInitService {
    private final OrderStatusRepository orderStatusRepository;

    @Autowired
    public OrderStatusServiceImpl(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public void dbInit() {
        List<OrderStatus> statuses = new ArrayList<>();

        statuses.add(new OrderStatus().setStatus(Status.ACCEPTED));
        statuses.add(new OrderStatus().setStatus(Status.SHIPPED));
        statuses.add(new OrderStatus().setStatus(Status.SHIPPING));

        this.orderStatusRepository.saveAllAndFlush(statuses);
    }

    @Override
    public boolean isDbInit() {
        return this.orderStatusRepository.count() > 0;
    }

    @Override
    public OrderStatus getAcceptedStatus() {
        return this.orderStatusRepository.findByStatus(Status.ACCEPTED);
    }
}
