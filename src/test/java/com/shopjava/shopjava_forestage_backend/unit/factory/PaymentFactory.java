package com.shopjava.shopjava_forestage_backend.unit.factory;

import com.shopjava.shopjava_forestage_backend.model.Payment;

public class PaymentFactory {
    public static Payment createEcpayCVS() {
        Payment payment = new Payment();
        payment.setProvider("ecpay");
        payment.setName("超商代碼");
        payment.setMethod("CVS");
        payment.setStatus(true);
        return payment;
    }
}
