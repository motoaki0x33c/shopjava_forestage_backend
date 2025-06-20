package com.shopjava.shopjava_forestage_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.CreateOrderRequest;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.GetOrderResponse;
import com.shopjava.shopjava_forestage_backend.model.*;
import com.shopjava.shopjava_forestage_backend.repository.LogisticsRepository;
import com.shopjava.shopjava_forestage_backend.repository.OrderRepository;
import com.shopjava.shopjava_forestage_backend.repository.PaymentRepository;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.PaymentAndLogisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LogisticsRepository logisticsRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到訂單"));
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public PaymentAndLogisticsResponse getUsablePaymentAndLogistics() {
        PaymentAndLogisticsResponse response = new PaymentAndLogisticsResponse();

        List<Map<String, Object>> payments = paymentRepository.getUsable().stream()
            .map(payment -> {
                Map<String, Object> paymentMap = new HashMap<>();
                paymentMap.put("id", payment.getId());
                paymentMap.put("provider", payment.getProvider());
                paymentMap.put("method", payment.getMethod());
                paymentMap.put("name", payment.getName());
                paymentMap.put("feeCost", payment.getFeeCost());
                return paymentMap;
            })
            .collect(Collectors.toList());

        List<Map<String, Object>> logistics = logisticsRepository.getUsable().stream()
            .map(logistic -> {
                Map<String, Object> logisticMap = new HashMap<>();
                logisticMap.put("id", logistic.getId());
                logisticMap.put("provider", logistic.getProvider());
                logisticMap.put("method", logistic.getMethod());
                logisticMap.put("cvsCode", logistic.getCvsCode());
                logisticMap.put("name", logistic.getName());
                logisticMap.put("shippingCost", logistic.getShippingCost());
                return logisticMap;
            })
            .collect(Collectors.toList());

        response.setPayments(payments);
        response.setLogistics(logistics);

        return response;
    }

    public Integer computeCartPrice(Cart cart, Payment payment, Logistics logistics) {
        List<CartProduct> cartProducts = cart.getCartProducts();
        if (cartProducts == null || cartProducts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "購物車內無任何有效商品");
        }

        cartProducts.stream()
            .filter(cartProduct -> !cartProduct.getProduct().getStatus())
            .findFirst()
            .ifPresent(cartProduct -> {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "購物車中含有下架商品：" + cartProduct.getProduct().getName());
            });

        if (!payment.getStatus()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "此付款方式已無法使用");
        if (!logistics.getStatus()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "此運送方式已無法使用");

        Integer productPrice = cartProducts.stream()
                .mapToInt(cartProduct -> cartProduct.getProduct().getPrice() * cartProduct.getQuantity())
                .sum();
        Integer shippingCost = logistics.getShippingCost();
        Integer feeCost = payment.getFeeCost();

        if (feeCost == null) feeCost = 0;
        if (shippingCost == null) shippingCost = 0;

        return productPrice + shippingCost + feeCost;
    }

    public Order create(CreateOrderRequest orderData, Payment payment, Logistics logistics) {
        Cart cart = cartService.getCart(orderData.getToken());
        
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setOrderStatus(Order.STATUS_UNCREATED);
        order.setPaymentStatus(Order.PAYMENT_STATUS_UNPAID);
        order.setLogisticsStatus(Order.LOGISTICS_STATUS_UNSHIPPED);
        order.setPaymentProvider(payment.getProvider());
        order.setPaymentMethod(payment.getMethod());
        order.setLogisticsProvider(logistics.getProvider());
        order.setLogisticsMethod(logistics.getMethod());
        order.setFeeCost(payment.getFeeCost());
        order.setShippingCost(logistics.getShippingCost());
        order.setTotalPrice(computeCartPrice(cart, payment, logistics));
        order.setCustomerName(orderData.getCustomerName());
        order.setCustomerEmail(orderData.getCustomerEmail());
        order.setCustomerPhone(orderData.getCustomerPhone());
        order.setCustomerAddress(orderData.getCustomerAddress());
        order.setPayment(payment);
        order.setLogistics(logistics);
        
        if (logistics.getMethod().equals("CVS")) {
            if (orderData.getCvsInfo() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "請選擇超商");
            }
            try {
                order.setCvsInfo(objectMapper.writeValueAsString(orderData.getCvsInfo()));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.toString());
            }
            order.setLogisticsCvsCode(logistics.getCvsCode());
        }

        List<OrderProduct> orderProducts = new ArrayList<>();
        for (CartProduct cartProduct : cart.getCartProducts()) {
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProduct(cartProduct.getProduct());
            orderProduct.setPrice(cartProduct.getProduct().getPrice());
            orderProduct.setQuantity(cartProduct.getQuantity());
            orderProducts.add(orderProduct);
        }

        order.setOrderProducts(orderProducts);
        cartService.deleteCart(cart.getId());

        return orderRepository.save(order);
    }

    public GetOrderResponse getOrderDetail(String orderNumber) {
        Order order = this.getOrderByNumber(orderNumber);

        GetOrderResponse response = new GetOrderResponse();

        Map<String, Object> cvsInfo = new HashMap<>();
        if (order.getLogisticsMethod().equals("CVS")) {
            try {
                cvsInfo = objectMapper.readValue(order.getCvsInfo(), Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.toString());
            }
        }

        List<Map<String, Object>> products = new ArrayList<>();
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Map<String, Object> product = new HashMap<>();
            product.put("price", orderProduct.getPrice());
            product.put("quantity", orderProduct.getQuantity());
            product.put("name", orderProduct.getProduct().getName());
            product.put("firstPhoto", orderProduct.getProduct().getFirstPhotoWithUrl());
            products.add(product);
        }

        response.setCreatedAt(order.getCreatedAt());
        response.setCustomerEmail(order.getCustomerEmail());
        response.setCustomerName(order.getCustomerName());
        response.setCustomerPhone(order.getCustomerPhone());
        response.setCustomerAddress(order.getCustomerAddress());
        response.setCvsInfo(cvsInfo);
        response.setLogisticsCvsCode(order.getLogisticsCvsCode());
        response.setLogisticsMethod(order.getLogisticsMethod());
        response.setLogisticsCvsCode(order.getLogisticsCvsCode());
        response.setLogisticsProvider(order.getLogisticsProvider());
        response.setLogisticsTrackingNumber(order.getLogisticsTrackingNumber());
        response.setLogisticsStatus(order.getLogisticsStatus());
        response.setOrderNumber(order.getOrderNumber());
        response.setOrderStatus(order.getOrderStatus());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentProvider(order.getPaymentProvider());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setPaymentTime(order.getPaymentTime());
        response.setFeeCost(order.getFeeCost());
        response.setShippingCost(order.getShippingCost());
        response.setTotalPrice(order.getTotalPrice());
        response.setOrderProducts(products);

        return response;
    }

    private String generateOrderNumber() {
        String orderNumber;
        do {
            // 數字開頭"1"，暫定一般訂單
            StringBuilder sb = new StringBuilder("1");
    
            // 時間(ddHHss)
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddHHss");
            sb.append(now.format(formatter));
    
            // 5碼隨機數字
            Random random = new Random();
            for (int i = 0; i < 5; i++) {
                sb.append(random.nextInt(10));
            }
    
            orderNumber = sb.toString();
            // 檢查訂單編號
        } while (orderRepository.existsByOrderNumber(orderNumber));
    
        return orderNumber;
    }

}
