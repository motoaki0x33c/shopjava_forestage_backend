package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    public List<Product> getAllProduct() {
        return productService.getAll();
    }

    @GetMapping("/{route}")
    public Product getProduct(@PathVariable String route) {
        return productService.getByRoute(route).orElse(null);
    }
}
