package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.controller.DTO.order.ComputeCartPriceRequest;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.PaymentAndLogisticsResponse;
import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.model.Logistics;
import com.shopjava.shopjava_forestage_backend.model.Payment;
import com.shopjava.shopjava_forestage_backend.service.CartService;
import com.shopjava.shopjava_forestage_backend.service.LogisticsService;
import com.shopjava.shopjava_forestage_backend.service.OrderService;
import com.shopjava.shopjava_forestage_backend.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
@Tag(name = "Order API", description = "訂單操作相關 API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private LogisticsService logisticsService;

    @PostMapping("/check/getUsablePaymentAndLogistics")
    @Operation(summary = "取得可用的金流和物流", description = "取得所有狀態為啟用的金流和物流選項")
    public PaymentAndLogisticsResponse getUsablePaymentAndLogistics() {
        return orderService.getUsablePaymentAndLogistics();
    }

    @PostMapping("/check/computeCartPrice")
    @Operation(summary = "計算購物車內金物流選擇後的訂單總金額", description = "")
    public Integer computeCartPrice(@Valid @RequestBody ComputeCartPriceRequest request) {
        Cart cart = cartService.getCart(request.getToken());
        Payment payment = paymentService.getById(request.getPaymentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到付款方式"));
        Logistics logistics = logisticsService.getById(request.getLogisticsId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到運送方式"));

        return orderService.computeCartPrice(cart, payment, logistics);
    }
}
