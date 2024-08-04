package com.proyectointegrador.msticket.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "paymentMethod")
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "category",nullable = false)
    private String category;

    @Column(name="detail",nullable = false)
    private String detail;

    public PaymentMethod(Long id) {
        this.Id = id;
    }
}
