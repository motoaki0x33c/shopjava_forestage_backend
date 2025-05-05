package com.shopjava.shopjava_forestage_backend.controller.DTO.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateCartProductRequest {
    @NotBlank(message = "token 不能為空")
    @Schema(description = "購物車 token", example = "abc-123-aaa")
    private String token;

    @NotNull(message = "productId 不能為空")
    @Schema(description = "產品 id", example = "133")
    private Long productId;

    @NotNull(message = "quantity 不能為空")
    @Min(message = "數量必須為 0 或正整數", value = 0)
    @Schema(description = "數量，如帶 0 則刪除該商品於購物車", example = "8")
    private Integer quantity;
}
