package com.shopjava.shopjava_forestage_backend.repository;

import com.shopjava.shopjava_forestage_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // 檢查訂單編號是否已存在
    boolean existsByOrderNumber(String orderNumber);
}
