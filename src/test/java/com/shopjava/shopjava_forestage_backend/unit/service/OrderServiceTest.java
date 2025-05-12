package com.shopjava.shopjava_forestage_backend.unit.service;

import com.shopjava.shopjava_forestage_backend.model.*;
import com.shopjava.shopjava_forestage_backend.service.OrderService;
import com.shopjava.shopjava_forestage_backend.unit.factory.CartFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.LogisticsFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.PaymentFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void testComputeCartPrice() {
        Cart fakeCart = CartFactory.createCartDefault();
        Payment fakePayment = PaymentFactory.createEcpayCVS();
        Logistics fakeLogistics = LogisticsFactory.createEcpayCVS();
        Product fakeProduct1 = ProductFactory.createDefault();
        Product fakeProduct2 = ProductFactory.createDefault();
        CartProduct fakeCartProduct1 = new CartProduct();
        CartProduct fakeCartProduct2 = new CartProduct();

        fakePayment.setFeeCost(10);
        fakeLogistics.setShippingCost(30);
        
        fakeProduct1.setPrice(130);
        fakeProduct2.setPrice(240);

        fakeCartProduct1.setProduct(fakeProduct1);
        fakeCartProduct1.setQuantity(3);
        fakeCartProduct2.setProduct(fakeProduct2);
        fakeCartProduct2.setQuantity(2);

        fakeCart.setCartProducts(Arrays.asList(fakeCartProduct1, fakeCartProduct2));

        Integer price = orderService.computeCartPrice(fakeCart, fakePayment, fakeLogistics);

        Assertions.assertEquals(10 + 30 + 130 * 3 + 240 * 2, price);

        System.out.println("testComputeCartPrice: OK");
    }
}
