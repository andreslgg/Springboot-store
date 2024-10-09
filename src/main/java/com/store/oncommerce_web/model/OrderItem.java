package com.store.oncommerce_web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    private String name;
    @Getter @Setter
    private int quantity;
    @Getter @Setter
    private double unitPrice;
    @Getter @Setter
    private double total;

    // Getters y setters
    // ...
}
