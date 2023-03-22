package com.example.aranoz.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity{
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Date date;

    @OneToOne
    private Product product;
}
