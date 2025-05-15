package com.shopjava.shopjava_forestage_backend.unit.service;

import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.model.CartProduct;
import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.repository.CartProductRepository;
import com.shopjava.shopjava_forestage_backend.repository.CartRepository;
import com.shopjava.shopjava_forestage_backend.service.CartService;
import com.shopjava.shopjava_forestage_backend.unit.factory.CartFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

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

    @Test
    public void testAddAndUpdateProductToCart() {
        Cart fakeCart = CartFactory.createCartDefault();
        Product fakeProduct = ProductFactory.createDefault();

        Mockito.when(cartProductRepository.findByCartAndProduct(fakeCart, fakeProduct)).thenReturn(Optional.empty());

        // 捕獲 cartProductRepository.save() 的內容
        ArgumentCaptor<CartProduct> cartProductCaptor = ArgumentCaptor.forClass(CartProduct.class);
        Mockito.when(cartProductRepository.save(cartProductCaptor.capture())).thenAnswer(i -> i.getArgument(0));

        cartService.addProductToCart(fakeCart, fakeProduct, 3);

        CartProduct capturedCartProduct = cartProductCaptor.getValue();

        Assertions.assertEquals(3, capturedCartProduct.getQuantity());

        System.out.println("testAddProductToCart: OK");



        Mockito.when(cartProductRepository.findByCartAndProduct(fakeCart, fakeProduct)).thenReturn(Optional.of(capturedCartProduct));

        cartService.updateCartProduct(fakeCart, fakeProduct, 5);

        capturedCartProduct = cartProductCaptor.getValue();

        Assertions.assertEquals(5, capturedCartProduct.getQuantity());

        System.out.println("testUpdateProductToCart: OK");
    }
}
