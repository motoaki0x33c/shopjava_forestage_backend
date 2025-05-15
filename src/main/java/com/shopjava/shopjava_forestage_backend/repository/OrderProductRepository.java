package com.shopjava.shopjava_forestage_backend.repository;

import com.shopjava.shopjava_forestage_backend.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
