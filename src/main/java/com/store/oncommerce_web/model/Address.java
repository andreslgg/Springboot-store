package com.store.oncommerce_web.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "address")
public class Address {

    @jakarta.persistence.Id
    @Setter @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Getter
    private String country;
    @Setter @Getter
    private String street;
    @Setter @Getter
    private String cityState;
    @Setter @Getter
    private String zipCode;
    @Setter @Getter
    private String state;
    @Setter @Getter
    private String city;
    @Setter @Getter
    private String internalNumber;
    @Setter @Getter
    private Long externalNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter @Getter
    private User user;


    // Getters and setters

}
