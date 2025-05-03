package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.repository.CartProductRepository;
import com.shopjava.shopjava_forestage_backend.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartProductRepository cartProductRepository;

    public Cart getCart(String token) {
        if (token == null || token.isEmpty())
            return createCart();
        else
            return cartRepository.findByToken(token).orElseGet(this::createCart);
    }

    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.setToken(UUID.randomUUID().toString());
        return cartRepository.save(cart);
    }
}
