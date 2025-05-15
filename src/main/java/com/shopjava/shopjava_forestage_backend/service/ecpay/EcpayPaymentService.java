package com.shopjava.shopjava_forestage_backend.service.ecpay;

import com.shopjava.shopjava_forestage_backend.model.Order;
import com.shopjava.shopjava_forestage_backend.model.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class EcpayPaymentService extends EcpayAbstract<Payment> {
    private Payment payment;

    @Value("${app.backendUrl}")
    private String backendUrl;

    @Value("${app.frontendUrl}")
    private String frontendUrl;

    public void setContractSetting(Payment payment) {
        if (payment.getProvider() != "ecpay" || payment.getSetting().isEmpty()) {
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
        params.put("ReturnURL", this.backendUrl + "/pay/ecpay/callback");
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1");
        params.put("ClientBackURL", this.frontendUrl + "/order/redirectFailPage");
        
        switch (this.payment.getMethod()) {
            case "CVS":
                params.put("PaymentType", "CVS");
                params.put("ClientRedirectURL", this.backendUrl + "/pay/ecpay/redirectSuccessPage");
                break;
            default:
                throw new RuntimeException("未定義的付款方式");
        }
        
        params.put("CheckMacValue", this.generateCheckMacValue(params));

        return this.generateFormHtml(params, this.getEcpayApiUrl() + "/Cashier/AioCheckOut/V5");
    }
}
