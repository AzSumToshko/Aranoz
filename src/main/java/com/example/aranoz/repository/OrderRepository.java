package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Order findByEmailAndDateOfCreation(String email, Date date);
    List<Order> getOrdersByUserId(String userId);
}
