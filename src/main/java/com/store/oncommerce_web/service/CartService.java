package com.store.oncommerce_web.service;


import com.store.oncommerce_web.model.Cart;
import com.store.oncommerce_web.model.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    void addItemToCart(CartItem item);
    List<CartItem> getCartItems();
    Cart getCart();
    void clearCart();

    void removeItemFromCart(Long productId, String color);
}
