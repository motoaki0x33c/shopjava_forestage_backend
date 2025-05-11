package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.controller.DTO.order.PaymentAndLogisticsResponse;
import com.shopjava.shopjava_forestage_backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
@Tag(name = "Order API", description = "訂單操作相關 API")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/getUsablePaymentAndLogistics")
    @Operation(summary = "取得可用的金流和物流", description = "取得所有狀態為啟用的金流和物流選項")
    public PaymentAndLogisticsResponse getUsablePaymentAndLogistics() {
        return orderService.getUsablePaymentAndLogistics();
    }
}
