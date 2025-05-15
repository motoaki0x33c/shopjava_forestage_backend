package com.shopjava.shopjava_forestage_backend.unit.service;

import com.shopjava.shopjava_forestage_backend.controller.DTO.order.CreateOrderRequest;
import com.shopjava.shopjava_forestage_backend.model.*;
import com.shopjava.shopjava_forestage_backend.repository.CartRepository;
import com.shopjava.shopjava_forestage_backend.repository.LogisticsRepository;
import com.shopjava.shopjava_forestage_backend.repository.OrderRepository;
import com.shopjava.shopjava_forestage_backend.repository.PaymentRepository;
import com.shopjava.shopjava_forestage_backend.service.OrderService;
import com.shopjava.shopjava_forestage_backend.unit.factory.CartFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.LogisticsFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.PaymentFactory;
import com.shopjava.shopjava_forestage_backend.unit.factory.ProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private OrderRepository orderRepository;

    @MockitoBean
    private PaymentRepository paymentRepository;

    @MockitoBean
    private LogisticsRepository logisticsRepository;

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

    @Test
    public void testCreate() {
        Cart fakeCart = CartFactory.createCartDefault();
        Payment fakePayment = PaymentFactory.createEcpayCVS();
        Logistics fakeLogistics = LogisticsFactory.createEcpayCVS();
        Product fakeProduct = ProductFactory.createDefault();
        CartProduct fakeCartProduct = new CartProduct();

        fakeProduct.setPrice(100);
        fakeCartProduct.setProduct(fakeProduct);
        fakeCartProduct.setQuantity(2);
        fakeCart.setCartProducts(Arrays.asList(fakeCartProduct));
        fakePayment.setFeeCost(10);
        fakeLogistics.setShippingCost(30);
        
        // 建立訂單
        CreateOrderRequest orderData = new CreateOrderRequest();
        orderData.setToken(fakeCart.getToken());
        orderData.setCustomerName("測試顧客");
        orderData.setCustomerEmail("test@example.com");
        orderData.setCustomerPhone("0912345678");
        orderData.setCustomerAddress("測試地址");

        Map<String, String> cvsInfo = new HashMap<>();
        cvsInfo.put("cvsCode", "1234");
        cvsInfo.put("cvsName", "測試超商");
        cvsInfo.put("cvsAddress", "測試超商地址");
        orderData.setCvsInfo(cvsInfo);

        Mockito.when(cartRepository.findByToken(fakeCart.getToken())).thenReturn(Optional.of(fakeCart));
        Mockito.when(orderRepository.existsByOrderNumber(Mockito.anyString())).thenReturn(false);
        
        // 使用 ArgumentCaptor 捕獲 save 方法的參數
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        Mockito.when(orderRepository.save(orderCaptor.capture())).thenAnswer(i -> i.getArgument(0));

        Order order = orderService.create(orderData, fakePayment, fakeLogistics);

        Order capturedOrder = orderCaptor.getValue();

        Assertions.assertNotNull(capturedOrder);
        Assertions.assertEquals(Order.STATUS_UNCREATED, capturedOrder.getOrderStatus());
        Assertions.assertEquals(Order.PAYMENT_STATUS_UNPAID, capturedOrder.getPaymentStatus());
        Assertions.assertEquals(Order.LOGISTICS_STATUS_UNSHIPPED, capturedOrder.getLogisticsStatus());
        Assertions.assertEquals(fakePayment.getProvider(), capturedOrder.getPaymentProvider());
        Assertions.assertEquals(fakePayment.getMethod(), capturedOrder.getPaymentMethod());
        Assertions.assertEquals(fakeLogistics.getProvider(), capturedOrder.getLogisticsProvider());
        Assertions.assertEquals(fakeLogistics.getMethod(), capturedOrder.getLogisticsMethod());
        Assertions.assertEquals(fakePayment.getFeeCost(), capturedOrder.getFeeCost());
        Assertions.assertEquals(fakeLogistics.getShippingCost(), capturedOrder.getShippingCost());
        Assertions.assertEquals(240, capturedOrder.getTotalPrice()); // 100 * 2 + 10 + 30
        Assertions.assertEquals("測試顧客", capturedOrder.getCustomerName());
        Assertions.assertEquals("test@example.com", capturedOrder.getCustomerEmail());
        Assertions.assertEquals("0912345678", capturedOrder.getCustomerPhone());
        Assertions.assertEquals("測試地址", capturedOrder.getCustomerAddress());
        Assertions.assertNotNull(capturedOrder.getCvsInfo());
        Assertions.assertEquals(1, capturedOrder.getOrderProducts().size());
        Assertions.assertEquals(2, capturedOrder.getOrderProducts().get(0).getQuantity());
        Assertions.assertEquals(fakeProduct, capturedOrder.getOrderProducts().get(0).getProduct());
        
        System.out.println("testCreate: OK");
    }
}
