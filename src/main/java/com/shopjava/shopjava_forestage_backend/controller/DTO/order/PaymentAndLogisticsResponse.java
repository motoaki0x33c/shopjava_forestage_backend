package com.shopjava.shopjava_forestage_backend.controller.DTO.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Schema(description = "金流和物流選項回應", example = """
    {
        "payments": [
            {
                "id": 1,
                "provider": "金流商",
                "method": "付款方式",
                "name": "金流名稱",
                "feeCost": 30
            }
        ],
        "logistics": [
            {
                "id": 1,
                "provider": "物流商",
                "method": "配送方式",
                "cvsCode": "超商代碼",
                "name": "物流名稱",
                "shippingCost": 60
            }
        ]
    }
    """)
public class PaymentAndLogisticsResponse {
    @Schema(description = "金流選項列表")
    private List<Map<String, Object>> payments;

    @Schema(description = "物流選項列表")
    private List<Map<String, Object>> logistics;
} 