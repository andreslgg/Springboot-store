package com.store.oncommerce_web.controller;

import com.store.oncommerce_web.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String showCheckoutPage(Model model) {
        BigDecimal subTotal = cartService.getTotalPrice();
        BigDecimal shippingCost = cartService.getShippingCost();
        BigDecimal taxAmount = cartService.getTaxAmount();
        BigDecimal totalAmount = subTotal.add(shippingCost).add(taxAmount);

        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shippingCost", shippingCost);
        model.addAttribute("taxAmount", taxAmount);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("content","checkout");


        return "layout";
    }

    @PostMapping("/complete-order")
    public String completeOrder(/* Añadir parámetros si es necesario */) {
        // Lógica para procesar el pedido
        // Por ejemplo, guardar el pedido en la base de datos, enviar confirmación por correo, etc.

        return "order-confirmation"; // Nombre del archivo HTML Thymeleaf para la vista de confirmación del pedido
    }
}
