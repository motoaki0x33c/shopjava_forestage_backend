package com.shopjava.shopjava_forestage_backend.service.ecpay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

abstract class EcpayAbstract<T> {
    @Autowired
    private ObjectMapper objectMapper;

    private String merchantID;
    private String hashKey;
    private String hashIV;

    abstract void setContractSetting(T logisticsOrPayment);

    protected String getEcpayApiLogUrl() { return "https://logistics-stage.ecpay.com.tw"; }
    protected String getEcpayApiPayUrl() { return "https://payment-stage.ecpay.com.tw"; }
    protected String getMerchantID() { return this.merchantID; }
    protected String getHashKey() { return this.hashKey; }
    protected String getHashIV() { return this.hashIV; }
    
    protected void setEcpaySetting(String settingString) {
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

    protected String generateFormHtml(Map<String, String> params, String url) {
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

    protected String generateCheckMacValue(Map<String, String> params) {
        // 1. 將參數依照第一個英文字母，由A到Z的順序來排序，需排除 CheckMacValue
        String[] keys = params.keySet().stream()
            .filter(key -> !key.equals("CheckMacValue"))
            .toArray(String[]::new);
        Arrays.sort(keys);

        // 2. 組合參數字串
        StringBuilder paramStr = new StringBuilder();
        for (String key : keys) {
            if (!paramStr.isEmpty()) {
                paramStr.append("&");
            }
            paramStr.append(key).append("=").append(params.get(key));
        }

        // 3. 參數最前面加上HashKey、最後面加上HashIV
        paramStr.insert(0, "HashKey=" + this.getHashKey() + "&");
        paramStr.append("&HashIV=" + this.getHashIV());

        try {
            // 4. URL encode
            String urlEncoded = URLEncoder.encode(paramStr.toString(), "UTF-8")
                    .replace("%2d", "-")
                    .replace("%5f", "_")
                    .replace("%2e", ".")
                    .replace("%21", "!")
                    .replace("%2a", "*")
                    .replace("%28", "(")
                    .replace("%29", ")");

            // 5. 轉為小寫
            urlEncoded = urlEncoded.toLowerCase();

            // 6. SHA256加密
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(urlEncoded.getBytes(StandardCharsets.UTF_8));

            // 7. 轉為大寫
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("生成檢查碼失敗", e);
        }
    }
}
