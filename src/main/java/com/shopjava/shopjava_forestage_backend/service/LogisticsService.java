package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.Logistics;
import com.shopjava.shopjava_forestage_backend.repository.LogisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LogisticsService {
    @Autowired
    private LogisticsRepository logisticsRepository;

    public Logistics getById(Long id) {
        return logisticsRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "找不到運送方式"));
    }
}
