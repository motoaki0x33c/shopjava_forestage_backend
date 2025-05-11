package com.shopjava.shopjava_forestage_backend.repository;

import com.shopjava.shopjava_forestage_backend.model.Logistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogisticsRepository extends JpaRepository<Logistics, Long> {
    @Query("SELECT log FROM Logistics log WHERE log.status = true")
    List<Logistics> getUsable();
}