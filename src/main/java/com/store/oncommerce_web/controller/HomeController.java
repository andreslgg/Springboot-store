package com.store.oncommerce_web.controller;

import com.store.oncommerce_web.model.Cart;
import com.store.oncommerce_web.model.Product;
import com.store.oncommerce_web.repository.ProductRepository;
import com.store.oncommerce_web.service.CartService;
import com.store.oncommerce_web.service.ProductService;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @GetMapping("/")
    public String home() {
        return "redirect:/products";
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return cartService.getCart(); // Retornamos el carrito desde el servicio
    }

    @GetMapping("/products")
    public String getProducts(Model model, @RequestParam(required = false) String searchTerm,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) Double minPrice,
                              @RequestParam(required = false) Double maxPrice,
                              @RequestParam(required = false) Double minRating) {

        // Recupera el mensaje flash si existe
        if (model.containsAttribute("message")) {
            String message = (String) model.getAttribute("message");
            model.addAttribute("cartMessage", message); // Pásalo a la vista
        }

        // Continuación del código
        List<Product> products = productRepository.findProducts(searchTerm, category, minPrice, maxPrice, minRating);
        for (Product product : products) {
            if(product.getColorImages() != null) {
                Map<String, String> colorImages = product.getColorImagesAsMap();
                product.setColorImagesFromMap(colorImages);
            }
        }
        model.addAttribute("products", products);

        List<String> categories = productService.findAllCategories();
        model.addAttribute("categories", categories);

        Product bestDiscountProduct = productService.getProductWithBestDiscount();
        if (bestDiscountProduct != null) {

            BigDecimal discountAmount = bestDiscountProduct.getPrice()
                    .multiply(bestDiscountProduct.getDiscount().divide(BigDecimal.valueOf(100)));

            BigDecimal discountedPrice = bestDiscountProduct.getPrice().subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);

            model.addAttribute("bestDiscountProduct", bestDiscountProduct);
            model.addAttribute("discountedPrice", discountedPrice);
        }
        Cart cart = cartService.getCart();
        cart.calculateTotalPrice();
        model.addAttribute("cartItems", cart);
        model.addAttribute("cart","cart");

        model.addAttribute("content","home");
        return "layout";
    }



}
