package com.shopjava.shopjava_forestage_backend.repository;

import com.shopjava.shopjava_forestage_backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("SELECT pay FROM Payment pay WHERE pay.status = true")
    List<Payment> getUsable();
}