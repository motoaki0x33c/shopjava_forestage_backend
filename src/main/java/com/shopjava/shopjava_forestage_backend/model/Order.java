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

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order {
    public static Integer STATUS_UNCREATED = 0;    // 訂單狀態：未建立
    public static Integer STATUS_CREATED = 1;      // 訂單狀態：已建立
    public static Integer STATUS_PROCESS = 2;      // 訂單狀態：處理中
    public static Integer STATUS_COMPLETE = 3;     // 訂單狀態：已完成
    public static Integer STATUS_CANCEL = 10;      // 訂單狀態：已取消

    public static Integer PAYMENT_STATUS_UNPAID = 0;    // 付款狀態：未付款
    public static Integer PAYMENT_STATUS_PAID = 1;      // 付款狀態：已付款
    public static Integer PAYMENT_STATUS_CANCEL = 10;   // 付款狀態：已取消
    public static Integer PAYMENT_STATUS_REFUND = 11;   // 付款狀態：已退款

    public static Integer LOGISTICS_STATUS_UNSHIPPED = 0;   // 運送狀態：未出貨
    public static Integer LOGISTICS_STATUS_SHIPPED = 1;     // 運送狀態：已出貨
    public static Integer LOGISTICS_STATUS_DELIVERED = 2;   // 運送狀態：已送達
    public static Integer LOGISTICS_STATUS_CANCEL = 10;     // 運送狀態：已取消
    public static Integer LOGISTICS_STATUS_RETURNED = 11;   // 運送狀態：已退貨

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
    private LocalDateTime paymentTime;

    private String cvsInfo;

    private String paymentLog;

    private String logisticsLog;

    private String logisticsTrackingNumber;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    private Logistics logistics;

    @ManyToOne
    private Payment payment;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts;
}