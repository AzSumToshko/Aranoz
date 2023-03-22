package com.example.aranoz.domain.entities;

import com.example.aranoz.domain.enums.Method;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "methods")
@Getter
public class PaymentMethod extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private Method method;

    public PaymentMethod setMethod(Method method) {
        this.method = method;
        return this;
    }
}
