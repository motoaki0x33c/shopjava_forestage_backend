package com.shopjava.shopjava_forestage_backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopjava.shopjava_forestage_backend.model.Logistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EcpayLogisticsService {
    @Autowired
    private ObjectMapper objectMapper;

    private final String ECPAY_API_URL = "https://logistics-stage.ecpay.com.tw";

    private Logistics logistics;
    private String merchantID;
    private String hashKey;
    private String hashIV;

    public void setLogistics(Logistics logistics) {
        this.logistics = logistics;
        String settingString = this.logistics.getSetting();
        if (settingString != null) {
            try {
                Map<String, String> setting = objectMapper.readValue(settingString, Map.class);
                this.merchantID = setting.get("MerchantID");
                this.hashKey = setting.get("HashKey");
                this.hashIV = setting.get("HashIV");
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e.toString());
            }
        }
    }

    public String getSelectCvsMapHtml() {
        String url = ECPAY_API_URL + "/Express/map";
        
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", this.merchantID);
        params.put("LogisticsType", "CVS");
        params.put("LogisticsSubType", this.logistics.getCvsCode());
        params.put("IsCollection", "N");
        params.put("ServerReplyURL", "http://localhost:8080/log/ecpay/redirectCartPageWithCvsInfo");

        // 構建表單HTML，使用 StringBuilder 方便處理長字串
        StringBuilder html = new StringBuilder();
        html.append("<form id='ecpayForm' action='").append(url).append("' method='post'>");
        params.forEach((key, value) -> {
            html.append("<input type='hidden' name='").append(key).append("' value='").append(value).append("'>");
        });
        html.append("</form>");
        html.append("<script>document.getElementById('ecpayForm').submit();</script>");

        return html.toString();
    }

    private String generateCheckMacValue(Map<String, String> params) {
        // TODO: 檢查碼
        return "";
    }
}
