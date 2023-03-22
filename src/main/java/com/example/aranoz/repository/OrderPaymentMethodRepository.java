package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.PaymentMethod;
import com.example.aranoz.domain.enums.Method;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentMethodRepository extends JpaRepository<PaymentMethod,String> {
    PaymentMethod findByMethod(Method method);
}
