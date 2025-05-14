package com.shopjava.shopjava_forestage_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String orderNumber;

    @NotNull
    private Integer orderStatus;

    @NotNull
    private Integer paymentStatus;

    @NotNull
    private Integer logisticsStatus;

    @NotBlank
    private String paymentProvider;

    @NotBlank
    private String paymentMethod;

    @NotBlank
    private String logisticsProvider;

    @NotBlank
    private String logisticsMethod;

    private String logisticsCvsCode;

    @NotNull
    @PositiveOrZero
    private Integer feeCost;

    @NotNull
    @PositiveOrZero
    private Integer shippingCost;

    @NotNull
    @PositiveOrZero
    private Integer totalPrice;

    @NotBlank
    private String customerName;

    @NotBlank
    private String customerEmail;

    @NotBlank
    private String customerPhone;

    @NotBlank
    private String customerAddress;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paymentTime;

    private String cvsInfo;

    private String paymentLog;

    private String logisticsLog;

    private String logisticsTrackingNumber;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    @ManyToOne
    private Logistics logistics;

    @ManyToOne
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;
}