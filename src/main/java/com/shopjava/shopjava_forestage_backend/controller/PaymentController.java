package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.model.Order;
import com.shopjava.shopjava_forestage_backend.service.OrderService;
import com.shopjava.shopjava_forestage_backend.service.ecpay.EcpayPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pay")
@Tag(name = "Payment API", description = "訂單操作相關 API")
public class PaymentController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private EcpayPaymentService ecpayPaymentService;

    @GetMapping("/toPaymentHtml/{orderNumber}")
    @Operation(summary = "轉跳至金流頁面", description = "回傳 form 表單 html，轉跳到第三方金流頁面")
    public String toPaymentHtml(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByNumber(orderNumber);

        if (order.getOrderStatus() != Order.STATUS_UNCREATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "訂單狀態不正確");
        }

        if (order.getPaymentProvider() != "ecpay") {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "不支援的付款方式");
        }

        ecpayPaymentService.setContractSetting(order.getPayment());
        String paymentHtml = ecpayPaymentService.createPayment(order);

        // 更新訂單狀態，避免重複結帳
        orderService.changeOrderStatus(order, Order.STATUS_CREATED);

        return paymentHtml;
    }
}
