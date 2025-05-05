package com.shopjava.shopjava_forestage_backend.controller.DTO.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetCartRequest {
    @NotBlank(message = "token 不能為空")
    @Schema(description = "購物車 token", example = "abc-123-aaa")
    private String token;
}
