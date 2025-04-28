package com.shopjava.shopjava_forestage_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)  // @CreatedDate、@LastModifiedDate 用，並記得於啟動類中加上 @EnableJpaAuditing
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "請輸入商品名稱")
    private String name;

    @NotNull(message = "請輸入商品價格")
    @PositiveOrZero(message = "商品價格不可為負數")
    private Integer price;

    private String sku;

    @NotBlank(message = "請輸入商品路徑")
    private String route;

    private String description;

    @NotNull(message = "醒輸入商品庫存")
    private Integer quantity;

    private Boolean status = false;

    private String firstPhoto;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
