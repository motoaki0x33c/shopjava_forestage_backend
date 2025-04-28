package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product update(Long id, Product up_data) {
        return productRepository.findById(id).map(product -> {
            product.setName(up_data.getName());
            product.setPrice(up_data.getPrice());
            product.setSku(up_data.getSku());
            product.setRoute(up_data.getRoute());
            product.setDescription(up_data.getDescription());
            product.setQuantity(up_data.getQuantity());
            product.setStatus(up_data.getStatus());
            product.setFirstPhoto(up_data.getFirstPhoto());
            return productRepository.save(product);
        }).orElse(null);
    }
}
