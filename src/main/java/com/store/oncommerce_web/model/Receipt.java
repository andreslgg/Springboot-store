package com.store.oncommerce_web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private UUID id; // Identificador único

    @Getter @Setter
    private String invoiceNumber;
    @Getter @Setter
    private String invoiceDate;
    @Getter @Setter
    private String customerName;
    @Getter @Setter
    private String customerAddress;
    @Getter @Setter
    private String customerEmail;
    @Getter @Setter
    private double subtotal;
    @Getter @Setter
    private double tax;
    @Getter @Setter
    private double shippingCost;
    @Getter @Setter
    private double total;
    @Getter @Setter
    private String qrCodeUrl; // Ruta del código QR generado

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "receipt_id")
    @Getter @Setter
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter @Getter
    private User user;


    // Getters y setters
    // ...
}
