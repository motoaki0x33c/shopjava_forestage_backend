package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.model.Order;
import com.shopjava.shopjava_forestage_backend.model.Payment;
import com.shopjava.shopjava_forestage_backend.service.OrderService;
import com.shopjava.shopjava_forestage_backend.service.ecpay.EcpayPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pay")
@Tag(name = "Payment API", description = "訂單操作相關 API")
public class PaymentController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private EcpayPaymentService ecpayPaymentService;

    @Value("${app.frontendUrl}")
    private String frontendUrl;

    private Logger log = LoggerFactory.getLogger(PaymentController.class);

    @GetMapping("/toPaymentHtml/{orderNumber}")
    @Operation(summary = "轉跳至金流頁面", description = "回傳 form 表單 html，轉跳到第三方金流頁面")
    public String toPaymentHtml(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByNumber(orderNumber);

        if (order.getOrderStatus() != Order.STATUS_UNCREATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "訂單狀態不正確");
        }

        if (!order.getPaymentProvider().equals("ecpay")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "不支援的付款方式");
        }

        ecpayPaymentService.setContractSetting(order.getPayment());
        String paymentHtml = ecpayPaymentService.createPayment(order);

        // 更新訂單狀態，避免重複結帳
        order.setOrderStatus(Order.STATUS_CREATED);
        orderService.updateOrder(order);

        return paymentHtml;
    }

    @PostMapping(value = "/ecpay/successRedirect", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Operation(summary = "成功完成綠界頁面操作", description = "於成功完成綠界頁面操作時綠界會呼叫此 API，並且轉回前端訂單成功頁面or失敗頁面")
    @ApiResponse(responseCode = "302", description = "http://domain-example.com/orderDetail/{orderNumber}")
    public ResponseEntity<Void> ecpaySuccessRedirect(@RequestParam Map<String, String> requestBody) {
        try {
            boolean isSuccess = false;
    
            String merchantTradeNo = requestBody.get("merchantTradeNo");
            if (merchantTradeNo == null) {
                return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/order/failed"))
                    .build();
            }

            Order order = orderService.getOrderByNumber(merchantTradeNo);
            Payment payment = order.getPayment();
            if (payment == null) {
                return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/order/failed"))
                    .build();
            }

            ecpayPaymentService.setContractSetting(payment);
            isSuccess = ecpayPaymentService.handleReturnData(requestBody, order);

            if (isSuccess) {
                return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/order/detail/" + order.getOrderNumber()))
                    .build();
            } else {
                return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(frontendUrl + "/order/failed"))
                    .build();
            }
        } catch (Exception e) {
            log.error("綠界回傳資料處理失敗", e);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/order/failed"))
                .build();
        }
    }

    // @PostMapping(value = "/ecpay/paidCallback", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    // @Operation(summary = "接收綠界背景付款完成回傳", description = "於付款完成時綠界會呼叫此 API")
    // public String ecpayPaidCallback(@RequestParam Map<String, String> requestBody) {
    //     String merchantTradeNo = requestBody.get("merchantTradeNo");
 
    //     if (merchantTradeNo == null) {
    //         return "0|MerchantTradeNo parameter error";
    //     }

    //     Order order = orderService.getOrderByNumber(merchantTradeNo);
    //     Payment payment = order.getPayment();
    //     if (payment == null) {
    //         return "0|Payment not found";
    //     }
        
    //     ecpayPaymentService.setContractSetting(payment);
    //     boolean isSuccess = ecpayPaymentService.handleReturnData(requestBody, order);

    //     if (isSuccess) {
    //         return "1|OK";
    //     } else {
    //         return "0|HandleReturnData failed";
    //     }
    // }
}
