package com.store.oncommerce_web.controller;

import com.store.oncommerce_web.model.Address;
import com.store.oncommerce_web.model.User;
import com.store.oncommerce_web.service.AddressService;
import com.store.oncommerce_web.service.CartService;
import com.store.oncommerce_web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
   private UserService userService;

    @Autowired
    private AddressService addressService;

    @GetMapping
    public String showCheckoutPage(Model model) {
        BigDecimal subTotal = cartService.getTotalPrice();
        BigDecimal shippingCost = cartService.getShippingCost();
        BigDecimal taxAmount = cartService.getTaxAmount();
        BigDecimal totalAmount = subTotal.add(shippingCost).add(taxAmount);

        User user = userService.getAuthenticatedUser();
        List<Address> Addresses = addressService.getAllAddressesByUserId(user.getId());
        model.addAttribute("addresses", Addresses);


        model.addAttribute("subTotal", subTotal);
        model.addAttribute("shippingCost", shippingCost);
        model.addAttribute("taxAmount", taxAmount);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("content","checkout");

        return "layout";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        User user = userService.getAuthenticatedUser();
        if (user != null) {
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("id", user.getId());

        }
    }

}
