package com.shopjava.shopjava_forestage_backend.controller.DTO.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComputeCartPriceRequest {
    @NotBlank(message = "token 不能為空")
    @Schema(description = "購物車 token", example = "abc-123-aaa")
    private String token;

    @NotNull
    @Schema(description = "金流 id", example = "19")
    private Long paymentId;

    @NotNull
    @Schema(description = "物流 id", example = "13")
    private Long logisticsId;
}
