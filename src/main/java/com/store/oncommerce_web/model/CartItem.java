package com.store.oncommerce_web.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class CartItem {
    @Getter @Setter
    private Product product;
    @Getter @Setter
    private String selectedColor;
    @Getter @Setter
    private int quantity;


        public CartItem(Product product, String selectedColor, int quantity) {
            this.product = product;
            this.selectedColor = selectedColor;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public String getColor() {
            return selectedColor;
        }

        public int getQuantity() {
            return quantity;
        }

        public BigDecimal getTotalPrice() {
            return product.getPrice().multiply(BigDecimal.valueOf(quantity));
        }
    }




