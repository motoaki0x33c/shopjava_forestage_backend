package com.shopjava.shopjava_forestage_backend.repository;

import com.shopjava.shopjava_forestage_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
