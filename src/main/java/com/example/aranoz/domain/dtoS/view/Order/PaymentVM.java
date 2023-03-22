package com.example.aranoz.domain.dtoS.view.Order;

import com.example.aranoz.domain.entities.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVM {

    private Order order;

    private String method;
}
