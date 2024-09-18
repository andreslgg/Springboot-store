package com.store.oncommerce_web.service;

import com.store.oncommerce_web.model.Cart;
import com.store.oncommerce_web.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {


    private Cart cart = new Cart();

    @Override
    public void addItemToCart(CartItem newItem) {
        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(newItem.getProduct().getId()) &&
                        Objects.equals(item.getSelectedColor(), newItem.getSelectedColor()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Si el producto ya existe con el mismo color, aumenta la cantidad
            CartItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
        } else {
            // Si no existe, lo añade como un nuevo ítem
            cart.addItem(newItem);
        }

        // Calcula el precio total del carrito
        cart.calculateTotalPrice();
    }


    @Override
    public List<CartItem> getCartItems() {
        return cart.getItems(); // Retorna la lista de ítems en el carrito
    }

    @Override
    public Cart getCart() {
        return cart; // Retorna el objeto del carrito
    }

    @Override
    public void clearCart() {
        cart.getItems().clear();
        cart.calculateTotalPrice();
    }

    public void removeItemFromCart(Long productId, String color) {
        // Verifica si el carrito no es nulo
        if (cart == null) {
            throw new IllegalStateException("El carrito no está inicializado.");
        }

        // Obtiene la lista de ítems del carrito
        List<CartItem> items = cart.getItems();

        // Recorre la lista para encontrar el primer ítem que coincida
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);

            // Verifica que el productId coincida exactamente
            if (item.getProduct().getId().equals(productId)) {

                // Si el color es una cadena vacía, eliminar solo si el color del ítem es null
                if (color.isEmpty() && item.getSelectedColor() == null) {
                    items.remove(i);
                    break; // Elimina solo el primer ítem que coincide
                }
                // Si el color no es vacío, eliminar solo si el color coincide exactamente
                else if (!color.isEmpty() && color.equals(item.getSelectedColor())) {
                    items.remove(i);
                    break; // Elimina solo el primer ítem que coincide
                }
            }
        }

        // Opcional: recalcular el total después de la eliminación
        cart.calculateTotalPrice();
    }

    @Override
    public BigDecimal getShippingCost() {
        return BigDecimal.valueOf(10.0);
    }

    @Override
    public BigDecimal getTotalPrice(){
        return cart.getTotalPrice();
    }

    @Override
    public Boolean hasProducts() {
        return !cart.getItems().isEmpty();

    }

    @Override
    public BigDecimal getTaxAmount() {
        BigDecimal taxRate = BigDecimal.valueOf(0.07); // Ejemplo: 7% de impuestos
        return getTotalPrice().multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

}