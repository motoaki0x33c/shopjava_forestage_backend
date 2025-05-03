package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/get")
    public Cart getCart(@RequestBody Map<String, String> body) {
        return cartService.getCart(body.get("token"));
    }
}
