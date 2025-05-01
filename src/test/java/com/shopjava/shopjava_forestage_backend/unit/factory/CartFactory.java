package com.shopjava.shopjava_forestage_backend.unit.factory;

import com.shopjava.shopjava_forestage_backend.model.Cart;

import java.util.UUID;

public class CartFactory {
    public static Cart createCartDefault() {
        Cart cart = new Cart();
        cart.setToken(UUID.randomUUID().toString());
        return cart;
    }
}
