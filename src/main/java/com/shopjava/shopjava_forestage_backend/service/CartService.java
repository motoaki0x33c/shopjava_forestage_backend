package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.model.CartProduct;
import com.shopjava.shopjava_forestage_backend.model.Product;
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

    public void addProductToCart(Cart cart, Product product, Integer quantity) {
        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product).orElse(new CartProduct());

        cartProduct.setCart(cart);
        cartProduct.setProduct(product);
        cartProduct.setQuantity((cartProduct.getQuantity() == null ? 0 : cartProduct.getQuantity()) + quantity);
        cartProductRepository.save(cartProduct);
    }

    public void updateCartProduct(Cart cart, Product product, Integer quantity) {
        CartProduct cartProduct = cartProductRepository.findByCartAndProduct(cart, product).orElse(null);

        if (cartProduct != null) {
            if (quantity <= 0) deleteCartProduct(cartProduct);
            else {
                cartProduct.setQuantity(quantity);
                cartProductRepository.save(cartProduct);
            }
        }
    }

    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    private void deleteCartProduct(CartProduct cartProduct) {
        cartProductRepository.delete(cartProduct);
    }

    private Cart createCart() {
        Cart cart = new Cart();
        cart.setToken(UUID.randomUUID().toString());
        return cartRepository.save(cart);
    }
}
