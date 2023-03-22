package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.OrderStatus;
import com.example.aranoz.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus,String> {
    OrderStatus findByStatus(Status accepted);
}
