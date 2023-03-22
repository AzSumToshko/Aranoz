package com.example.aranoz.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity{

    //form information
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = true)
    private String company;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String addressOne;

    @Column(nullable = true)
    private String addressTwo;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = true)
    private String message;
    //

    //data information
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Double total;

    //datata nagore e cqlata v checkout vm-a
    @OneToOne
    private OrderStatus status;

    @Column(columnDefinition = "LONGTEXT",nullable = false)
    private String ProductAndQuantities;

    @OneToOne
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Date dateOfCreation;
    //
}
