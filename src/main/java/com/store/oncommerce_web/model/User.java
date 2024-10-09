package com.store.oncommerce_web.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter @Getter
    private Long id;
    @Setter @Getter
    @Column(nullable = false, unique = true)
    private String email;
    @Setter @Getter
    @Column(nullable = false)
    private String password;
    @Setter @Getter
    @Column(nullable = false)
    private String firstName;
    @Setter @Getter
    @Column(nullable = false)
    private String lastName;

}
