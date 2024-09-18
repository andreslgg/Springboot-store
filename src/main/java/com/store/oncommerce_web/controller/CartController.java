package com.store.oncommerce_web.controller;

import com.store.oncommerce_web.model.Cart;
import com.store.oncommerce_web.model.CartItem;
import com.store.oncommerce_web.model.Product;

import com.store.oncommerce_web.service.CartService;
import com.store.oncommerce_web.service.ProductService;
import jakarta.servlet.ServletContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;


    @PostMapping("/cart/clear")
    public String clearCart(Model model) {
        cartService.clearCart();
        Cart cart = cartService.getCart();
        model.addAttribute("cartItems", cart);

        return "fragments/cart :: cartfragments";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "color", required = false) String color,
                            @RequestParam("quantity") int quantity,
                            Model model) {
        // Obtener el producto por ID
        Product product = productService.findProductById(productId);
        CartItem cartItem = new CartItem(product, color, quantity);
        cartService.addItemToCart(cartItem);
        Cart cart = cartService.getCart();
        model.addAttribute("cartItems", cart);
        model.addAttribute("shippingCost", cartService.getShippingCost());


        model.addAttribute("hasProducts", cartService.hasProducts());

        return "fragments/cart :: cartfragments";
    }

    @GetMapping("/cart")
    public String getCart(Model model) {
        Cart cart = cartService.getCart();
        model.addAttribute("cartItems", cart);
        model.addAttribute("content", "cart");
        model.addAttribute("hasProducts", cartService.hasProducts());

        return "layout";
    }

    @PostMapping("/cart/remove")
    public String removeItem(@RequestParam("productId") Long productId,
                             @RequestParam(value = "color", required = false, defaultValue = "") String color,
                             Model model) {
        // Si color es nulo o vacío, se asigna un valor predeterminado
        // defaultValue="" maneja este caso automáticamente, por lo que la verificación explícita puede no ser necesaria

        cartService.removeItemFromCart(productId, color);
        Cart cart = cartService.getCart();
        cart.calculateTotalPrice();
        model.addAttribute("cartItems", cart);

        model.addAttribute("shippingCost", cartService.getShippingCost());
        model.addAttribute("hasProducts", cartService.hasProducts());

        return "fragments/cart :: cartfragments"; // Devuelve el fragmento actualizado
    }

}

