package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.controller.DTO.order.ComputeCartPriceRequest;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.CreateOrderRequest;
import com.shopjava.shopjava_forestage_backend.controller.DTO.order.PaymentAndLogisticsResponse;
import com.shopjava.shopjava_forestage_backend.model.Cart;
import com.shopjava.shopjava_forestage_backend.model.Logistics;
import com.shopjava.shopjava_forestage_backend.model.Order;
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
    public Integer computeCartPrice(@Valid @RequestBody ComputeCartPriceRequest requestBody) {
        Cart cart = cartService.getCart(requestBody.getToken());
        Payment payment = paymentService.getById(requestBody.getPaymentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到付款方式"));
        Logistics logistics = logisticsService.getById(requestBody.getLogisticsId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到運送方式"));

        return orderService.computeCartPrice(cart, payment, logistics);
    }

    @PostMapping("/create")
    @Operation(summary = "建立訂單", description = "成功將會回傳訂單編號")
    public String create(@Valid @RequestBody CreateOrderRequest requestBody) {
        Payment payment = paymentService.getById(requestBody.getPaymentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到付款方式"));
        Logistics logistics = logisticsService.getById(requestBody.getLogisticsId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到運送方式"));

        if (logistics.getMethod().equals("CVS")) {
            if (requestBody.getCvsInfo() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "請選擇超商");
            }
        }

        Order order = orderService.create(requestBody, payment, logistics);

        return order.getOrderNumber();
    }
}
