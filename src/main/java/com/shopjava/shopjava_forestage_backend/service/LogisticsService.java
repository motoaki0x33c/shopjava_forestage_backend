package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.Logistics;
import com.shopjava.shopjava_forestage_backend.repository.LogisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LogisticsService {
    @Autowired
    private LogisticsRepository logisticsRepository;

    public Optional<Logistics> getById(Long id) {
        return logisticsRepository.findById(id);
    }
}
