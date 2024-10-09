package com.store.oncommerce_web.model;

import lombok.Getter;
import lombok.Setter;

public class UserDTO {
    @Setter @Getter
    private String email;
    @Setter @Getter
    private String password;
    @Setter @Getter
    private String firstName;
    @Setter @Getter
    private String lastName;
    @Setter @Getter
    private String confirmPassword;

    // Getters y Setters

}
