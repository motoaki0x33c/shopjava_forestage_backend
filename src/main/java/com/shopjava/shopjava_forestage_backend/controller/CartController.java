package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.controller.DTO.cart.AddCartProductRequest;
import com.shopjava.shopjava_forestage_backend.controller.DTO.cart.GetCartRequest;
import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.service.CartService;
import com.shopjava.shopjava_forestage_backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/cart")
@Tag(name = "Cart API", description = "購物車操作相關 API")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @PostMapping("/get")
    @Operation(summary = "取得購物車", description = "根據 token 取得購物車資料，如找不到則回傳一筆新購物車")
    public Cart getCart(@RequestBody GetCartRequest request) {
        return cartService.getCart(request.getToken());
    }

    @PostMapping("/addProduct")
    @Operation(summary = "更新購物車商品數量", description = "更新一項購物車內商品的數量")
    public Cart addProductToCart(@Valid @RequestBody AddCartProductRequest request) {
        Cart cart = cartService.getCart(request.getToken());
        Product product = productService.getById(request.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到商品"));

        cartService.addProductToCart(cart, product, request.getQuantity());

        return cart;
    }
}
