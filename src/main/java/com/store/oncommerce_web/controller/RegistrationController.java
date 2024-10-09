package com.store.oncommerce_web.controller;

import com.store.oncommerce_web.model.UserDTO;
import com.store.oncommerce_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "createUser"; // Nombre de la vista Thymeleaf del formulario
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") UserDTO userDto, Model model) {
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match");
            return "register";
        }

        // Verificar si el usuario ya existe
        if (userService.existsByEmail(userDto.getEmail())) {
            model.addAttribute("emailError", "Email already registered");
            return "register";
        }

        // Registrar el usuario
        userService.registerNewUser(userDto);
        return "redirect:/login?success";
    }
}
