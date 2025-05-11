package com.shopjava.shopjava_forestage_backend.service;

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
}
