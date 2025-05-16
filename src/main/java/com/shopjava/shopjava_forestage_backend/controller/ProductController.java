package com.shopjava.shopjava_forestage_backend.controller;

import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/product")
@Tag(name = "Product API", description = "商品操作相關 API")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("")
    @Operation(summary = "取得所有商品", description = "")
    public List<Product> getAllProduct() {
        return productService.getAll();
    }

    @GetMapping("/{route}")
    @Operation(summary = "取得指定商品", description = "根據 route 取得商品資料")
    public Product getProduct(@PathVariable String route) {
        return productService.getByRoute(route);
    }
}
