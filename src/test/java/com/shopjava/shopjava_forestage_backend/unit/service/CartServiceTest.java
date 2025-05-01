package com.shopjava.shopjava_forestage_backend.unit.service;

import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.repository.CartProductRepository;
import com.shopjava.shopjava_forestage_backend.repository.CartRepository;
import com.shopjava.shopjava_forestage_backend.service.CartService;
import com.shopjava.shopjava_forestage_backend.unit.factory.CartFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CartServiceTest {
    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private CartProductRepository cartProductRepository;

    @Autowired
    private CartService cartService;

    @Test
    public void testGetCartIfTokenNotExist() {
        Cart fakeCart = CartFactory.createCartDefault();

        Mockito.when(cartRepository.save(any(Cart.class))).thenReturn(fakeCart);

        Cart cart = cartService.getCart(null);

        Assertions.assertNotNull(cart);
        Assertions.assertNotNull(cart.getToken());

        System.out.println("testGetCartIfTokenNotExist: OK");
    }
}
