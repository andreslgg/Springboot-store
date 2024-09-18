package com.store.oncommerce_web.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal totalPrice;

    public void addItem(CartItem item) {
        items.add(item);
        calculateTotalPrice();
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        calculateTotalPrice();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void calculateTotalPrice() {
        totalPrice = items.stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}


    // Métodos adicionales como eliminar un ítem, obtener total, etc.

