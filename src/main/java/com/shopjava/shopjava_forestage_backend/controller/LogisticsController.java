package com.shopjava.shopjava_forestage_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopjava.shopjava_forestage_backend.model.Logistics;
import com.shopjava.shopjava_forestage_backend.service.ecpay.EcpayLogisticsService;
import com.shopjava.shopjava_forestage_backend.service.LogisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/log")
@Tag(name = "Logisticd API", description = "物流操作相關 API")
public class LogisticsController {
    @Autowired
    private EcpayLogisticsService ecpayLogisticsService;

    @Autowired
    private LogisticsService logisticsService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${app.frontendUrl}")
    private String frontendUrl;

    @GetMapping("/ecpay/selectCvsMap/{logId}")
    @Operation(summary = "取得綠界超商電子地圖", description = "取得綠界超商電子地圖並回傳 form 表單 html")
    public String showEcpaySelectCvsMap(@PathVariable Long logId) {
        Logistics logistics = logisticsService.getById(logId);

        ecpayLogisticsService.setContractSetting(logistics);
        return ecpayLogisticsService.getSelectCvsMapHtml();
    }

    @PostMapping(value = "/ecpay/redirectCartPageWithCvsInfo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Operation(summary = "導向購物車頁面並帶上超商資訊", description = "網址參數中 cvsInfo 為 base64 編碼處理，解碼後格式為 JSON")
    @ApiResponse(responseCode = "302", description = "http://domain-example.com/cart?cvsInfo=eyJjdnNJZCI6IjAwNjU5OCIsImN2c05hbWUiOiLlj7DphqvlupciLCJjdnNBZGRyZXNzIjoi5Y+w5YyX5biC5Lit5q2j5Y2A5Lit5bGx5Y2X6Lev77yX6Jmf77yR5qiTIn0=")
    public ResponseEntity<Void> redirectCartPageWithCvsInfo(@RequestParam Map<String, String> requestBody) {
        Map<String, String> cvsInfo = new HashMap<>();
        String cvsInfoString;

        cvsInfo.put("cvsId", requestBody.get("CVSStoreID"));
        cvsInfo.put("cvsName", requestBody.get("CVSStoreName"));
        cvsInfo.put("cvsAddress", requestBody.get("CVSAddress"));

        try {
            cvsInfoString = objectMapper.writeValueAsString(cvsInfo);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.toString());
        }

        String base64Html = Base64.getEncoder().encodeToString(cvsInfoString.getBytes());
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/cart?cvsInfo=" + base64Html))
                .build();
    }
}
