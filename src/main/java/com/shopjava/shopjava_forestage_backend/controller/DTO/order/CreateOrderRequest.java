package com.shopjava.shopjava_forestage_backend.controller.DTO.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "token 不能為空")
    @Schema(description = "購物車 token", example = "abc-123-aaa")
    private String token;

    @NotNull(message = "paymentId 不能為空")
    @Schema(description = "金流 id", example = "19")
    private Long paymentId;

    @NotNull(message = "logisticsId 不能為空")
    @Schema(description = "物流 id", example = "13")
    private Long logisticsId;
    
    @NotBlank(message = "customerName 不能為空")
    @Schema(description = "顧客姓名", example = "張三")
    private String customerName;

    @NotBlank(message = "customerEmail 不能為空")
    @Schema(description = "顧客 email", example = "test@gmail.com")
    private String customerEmail;

    @NotBlank(message = "customerPhone 不能為空")
    @Schema(description = "顧客電話", example = "0912345678")
    private String customerPhone;
    
    @NotBlank(message = "customerAddress 不能為空")
    @Schema(description = "顧客地址", example = "台北市中山區中山路1號")
    private String customerAddress;

    @Schema(description = "超商代碼", example = """
        {
            "cvsCode": "1234",
            "cvsName": "超商",
            "cvsAddress": "台北市中山區中山路1號"
        }
        """)
    private Map<String, String> cvsInfo;
}
