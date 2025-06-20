package com.shopjava.shopjava_forestage_backend.service.ecpay;

import com.shopjava.shopjava_forestage_backend.model.Logistics;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EcpayLogisticsService extends EcpayAbstract<Logistics> {
    private Logistics logistics;

    @Value("${app.backendUrl}")
    private String backendUrl;

    public void setContractSetting(Logistics logistics) {
        if (!logistics.getProvider().equals("ecpay") || logistics.getSetting() == null) {
            throw new RuntimeException("物流提供商或設定不正確");
        }
        this.logistics = logistics;
        this.setEcpaySetting(logistics.getSetting());
    }

    public String getSelectCvsMapHtml() {
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", this.getMerchantID());
        params.put("LogisticsType", "CVS");
        params.put("LogisticsSubType", this.logistics.getCvsCode());
        params.put("IsCollection", "N");
        params.put("ServerReplyURL", this.backendUrl + "/log/ecpay/redirectCartPageWithCvsInfo");

        return this.generateFormHtml(params, this.getEcpayApiUrl() + "/Express/map");
    }
}
