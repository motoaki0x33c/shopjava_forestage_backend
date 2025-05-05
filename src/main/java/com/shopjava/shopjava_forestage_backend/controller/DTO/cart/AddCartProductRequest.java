package com.shopjava.shopjava_forestage_backend.controller.DTO.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class AddCartProductRequest {
    @Schema(description = "購物車 token", example = "abc-123-aaa")
    private String token;

    @NotNull(message = "productId 不能為空")
    @Schema(description = "產品 id", example = "133")
    private Long productId;

    @NotNull(message = "quantity 不能為空")
    @Min(message = "數量必須大於 1", value = 1)
    @Schema(description = "數量", example = "3")
    private Integer quantity;
}
