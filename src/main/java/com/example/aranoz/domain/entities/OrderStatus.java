package com.example.aranoz.domain.entities;

import com.example.aranoz.domain.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "statuses")
@Getter
public class OrderStatus extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private Status status;

    public OrderStatus setStatus(Status status) {
        this.status = status;
        return this;
    }
}
