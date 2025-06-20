package com.shopjava.shopjava_forestage_backend.controller.DTO.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Schema(description = "訂單明細回應", example = """
    {
        "createdAt": "2025-05-10 10:21:56",
        "customerAddress": "台北市中正區中山南路7號1樓",
        "customerEmail": "ddd@ddd.dd",
        "customerName": "收件人",
        "customerPhone": "0912345678",
        "cvsInfo": {
            "cvsId": "006598",
            "cvsName": "台醫店",
            "cvsAddress": "台北市中正區中山南路7號1樓"
        },
        "logisticsCvsCode": "FAMI",
        "logisticsMethod": "CVS",
        "logisticsProvider": "ecpay",
        "logisticsStatus": 0,
        "logisticsTrackingNumber": "",
        "orderNumber": "116220957313",
        "orderStatus": 1,
        "paymentMethod": "CVS",
        "paymentProvider": "ecpay",
        "paymentStatus": 0,
        "paymentTime": "",
        "feeCost": 0,
        "shippingCost": 0,
        "totalPrice": 199,
        "orderProducts": [
            {
                "price": 199,
                "quantity": 1,
                "name": "PANTENE 潘婷乳液修護去屑洗髮乳700G",
                "firstPhoto": "/img/product/000001_1668593049.webp"
            }
        ]
    }
    """)
public class GetOrderResponse {
    @Schema(description = "訂單成立時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "顧客地址")
    private String customerAddress;

    @Schema(description = "顧客信箱")
    private String customerEmail;

    @Schema(description = "顧客姓名")
    private String customerName;

    @Schema(description = "顧客電話")
    private String customerPhone;

    @Schema(description = "超商資訊", example = """
        {
            "cvsCode": "1234",
            "cvsName": "超商",
            "cvsAddress": "台北市中山區中山路1號"
        }
        """)
    private Map<String, Object> cvsInfo;

    @Schema(description = "物流超商類型")
    private String logisticsCvsCode;

    @Schema(description = "物流類型")
    private String logisticsMethod;

    @Schema(description = "物流供應商")
    private String logisticsProvider;

    @Schema(description = "物流狀態")
    private Integer logisticsStatus;

    @Schema(description = "物流追蹤代碼")
    private String logisticsTrackingNumber;

    @Schema(description = "訂單編號")
    private String orderNumber;

    @Schema(description = "訂單狀態")
    private Integer orderStatus;

    @Schema(description = "金流類型")
    private String paymentMethod;

    @Schema(description = "金流供應商")
    private String paymentProvider;

    @Schema(description = "金流狀態")
    private Integer paymentStatus;

    @Schema(description = "付款時間")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;

    @Schema(description = "手續費")
    private Integer feeCost;

    @Schema(description = "運費")
    private Integer shippingCost;

    @Schema(description = "訂單金額")
    private Integer totalPrice;

    @Schema(description = "商品資訊", example = """
        [
            {
                "price": 199,
                "quantity": 1,
                "name": "PANTENE 潘婷乳液修護去屑洗髮乳700G",
                "firstPhoto": "/img/product/000001_1668593049.webp"
            }
        ]
        """)
    private List<Map<String, Object>> orderProducts;
}