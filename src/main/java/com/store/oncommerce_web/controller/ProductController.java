package com.store.oncommerce_web.controller;


import com.store.oncommerce_web.model.Product;
import com.store.oncommerce_web.repository.ProductRepository;
import com.store.oncommerce_web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private  ProductService productService;
@Autowired
private ProductRepository productRepository;

    @GetMapping("/fetch-products")
    public List<Product> fetchProducts() {
        return productService.fetchAndSaveProducts();
    }

    @GetMapping("/api/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
