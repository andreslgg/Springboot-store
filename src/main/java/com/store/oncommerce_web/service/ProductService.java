package com.store.oncommerce_web.service;

import com.store.oncommerce_web.model.Product;
import com.store.oncommerce_web.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private  ProductRepository productRepository;

    private final String API_URL = "https://fakestoreapi.com/products?limit=50";

    public List<Product> fetchAndSaveProducts() {
        RestTemplate restTemplate = new RestTemplate();
        Product[] products = restTemplate.getForObject(API_URL, Product[].class);
        System.out.println(Arrays.toString(products)); // Verifica los datos recibidos

        if (products != null) {
            for (Product product : products) {
                // Truncar la descripción si excede los 255 caracteres
                if (product.getDescription() != null && product.getDescription().length() > 255) {
                    product.setDescription(product.getDescription().substring(0, 255));
                }

            }

            List<Product> productList = Arrays.asList(products);

            // Guardar todos los productos en la base de datos
            return productRepository.saveAll(productList);
        }
        return null;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public List<String> findAllCategories() {
        return productRepository.findDistinctCategories();
    }

    public Product getProductWithBestDiscount() {
        return productRepository.findProductWithBestDiscount();
    }

    public Product findProductById(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        return product.orElse(null);
    }

}
