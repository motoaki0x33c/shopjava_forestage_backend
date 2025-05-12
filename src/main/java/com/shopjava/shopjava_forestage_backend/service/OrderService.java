package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.*;
import com.shopjava.shopjava_forestage_backend.repository.LogisticsRepository;
import com.shopjava.shopjava_forestage_backend.repository.PaymentRepository;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.PaymentAndLogisticsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private LogisticsRepository logisticsRepository;

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
        if (cart.getCartProducts().isEmpty()) throw new RuntimeException("購物車內無任何有效商品");

        cart.getCartProducts().stream()
            .filter(cartProduct -> !cartProduct.getProduct().getStatus())
            .findFirst()
            .ifPresent(cartProduct -> {
                throw new RuntimeException("購物車中含有下架商品：" + cartProduct.getProduct().getName());
            });

        if (!payment.getStatus()) throw new RuntimeException("此付款方式已無法使用");
        if (!logistics.getStatus()) throw new RuntimeException("此運送方式已無法使用");

        Integer productPrice = cart.getCartProducts().stream()
                .mapToInt(cartProduct -> cartProduct.getProduct().getPrice() * cartProduct.getQuantity())
                .sum();
        Integer shippingCost = logistics.getShippingCost();
        Integer feeCost = payment.getFeeCost();

        if (feeCost == null) feeCost = 0;
        if (shippingCost == null) shippingCost = 0;

        return productPrice + shippingCost + feeCost;
    }
}
