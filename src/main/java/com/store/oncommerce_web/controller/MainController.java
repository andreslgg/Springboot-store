package com.store.oncommerce_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private String message = "Texto inicial";

    @GetMapping("/m")
    public String showMainPage(Model model) {
        model.addAttribute("message", message);
        return "main";
    }

    @PostMapping("/updateText")
    public String updateText(Model model) {
        // Actualiza el mensaje (puedes hacer esto dinámicamente si quieres)
        message = "Texto actualizado en " + System.currentTimeMillis();
        model.addAttribute("message", message);
        // Devuelve el fragmento de texto actualizado
        return "fragments/textFragment :: text";
    }
}

