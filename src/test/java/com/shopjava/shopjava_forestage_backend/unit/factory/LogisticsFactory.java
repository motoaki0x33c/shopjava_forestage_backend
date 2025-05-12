package com.shopjava.shopjava_forestage_backend.unit.factory;

import com.shopjava.shopjava_forestage_backend.model.Logistics;

public class LogisticsFactory {
    public static Logistics createEcpayCVS() {
        Logistics logistics = new Logistics();
        logistics.setProvider("ecpay");
        logistics.setName("超商取貨 - 全家");
        logistics.setMethod("CVS");
        logistics.setCvsCode("fami");
        logistics.setStatus(true);
        return logistics;
    }
}
