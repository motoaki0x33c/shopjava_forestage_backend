package com.shopjava.shopjava_forestage_backend.service.ecpay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopjava.shopjava_forestage_backend.model.Order;
import com.shopjava.shopjava_forestage_backend.model.Payment;
import com.shopjava.shopjava_forestage_backend.service.OrderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EcpayPaymentService extends EcpayAbstract<Payment> {
    private Payment payment;

    private Logger log = LoggerFactory.getLogger(EcpayPaymentService.class);

    @Value("${app.backendUrl}")
    private String backendUrl;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    public void setContractSetting(Payment payment) {
        if (!payment.getProvider().equals("ecpay") || payment.getSetting() == null) {
            throw new RuntimeException("金流提供商或設定不正確");
        }
        this.payment = payment;
        this.setEcpaySetting(payment.getSetting());
    }

    public String createPayment(Order order) {
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", this.getMerchantID());
        params.put("MerchantTradeNo", order.getOrderNumber());
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("TotalAmount", order.getTotalPrice().toString());
        params.put("TradeDesc", "測試交易");
        params.put("ItemName", "測試商品");
        params.put("PaymentType", "aio");
        params.put("ReturnURL", this.backendUrl + "/pay/ecpay/paidCallback");
        params.put("EncryptType", "1");
        params.put("ClientBackURL", this.backendUrl + "/pay/ecpay/failedRedirect");
        
        switch (this.payment.getMethod()) {
            case "CVS":
                params.put("ChoosePayment", "CVS");
                params.put("ClientRedirectURL", this.backendUrl + "/pay/ecpay/successRedirect");
                break;
            default:
                throw new RuntimeException("未定義的付款方式");
        }
        
        params.put("CheckMacValue", this.generateCheckMacValue(params));

        return this.generateFormHtml(params, this.getEcpayApiPayUrl() + "/Cashier/AioCheckOut/V5");
    }

    public boolean handleReturnData(Map<String, String> requestBody, Order order) {
        if (requestBody.get("MerchantID") == null || !requestBody.get("MerchantID").equals(this.getMerchantID())) {
            return false;
        }

        if (!requestBody.get("PaymentType").equals(order.getPaymentMethod())) {
            return false;
        }
        
        if (!requestBody.get("CheckMacValue").equals(this.generateCheckMacValue(requestBody))) {
            return false;
        }

        if (requestBody.get("SimulatePaid").equals("1")) {
            // 模擬付款
            // return false;
        }

        log.info("訂單：{}，綠界操作完成回傳資料：{}", order.getOrderNumber(), requestBody);

        if (order.getOrderStatus() == Order.STATUS_CREATED) {
            order.setOrderStatus(Order.STATUS_PROCESS);
        }

        switch (order.getPaymentMethod()) {
            case "CVS":
                if (!requestBody.get("RtnCode").equals("10100073")) {
                    // 取號失敗
                    return false;
                }
                break;
            default:
                return false;
        }

        try {
            order.setPaymentLog(objectMapper.writeValueAsString(requestBody));
        } catch (JsonProcessingException e) {;
            return false;
        }

        orderService.updateOrder(order);
        return true;
    }
}
