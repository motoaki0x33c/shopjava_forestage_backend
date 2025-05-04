package com.shopjava.shopjava_forestage_backend.unit.service;

import com.shopjava.shopjava_forestage_backend.service.ProductService;
import com.shopjava.shopjava_forestage_backend.unit.factory.ProductFactory;
import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class ProductServiceTest {

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    void testCreate() {
        Product product = ProductFactory.createDefault();

        Product savedProduct = productService.create(product);

        // 捕獲 productRepository.save() 時的 Product.class 內容
        ArgumentCaptor<Product> cartProductCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(cartProductCaptor.capture());
        Product capturedProduct = cartProductCaptor.getValue();

        Assertions.assertNotNull(capturedProduct);
        Assertions.assertEquals(product.getName(), capturedProduct.getName());
        Assertions.assertEquals(product.getPrice(), capturedProduct.getPrice());
        Assertions.assertEquals(product.getSku(), capturedProduct.getSku());
        Assertions.assertEquals(product.getRoute(), capturedProduct.getRoute());
        Assertions.assertEquals(product.getQuantity(), capturedProduct.getQuantity());
        
        System.out.println("testCreate: OK");
    }

    @Test
    void testGetByRoute_ExistingProduct() {
        Product product = ProductFactory.createDefault();

        Mockito.when(productRepository.findByRoute(product.getRoute())).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getByRoute(product.getRoute());

        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product.getName(), foundProduct.get().getName());

        System.out.println("testGetByRoute_ExistingProduct: OK");
    }

    @Test
    void testGetByRoute_NonExistingProduct() {
        Mockito.when(productRepository.findByRoute("NonExisting")).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productService.getByRoute("NonExisting");

        Assertions.assertFalse(foundProduct.isPresent());

        System.out.println("testGetByRoute_NonExistingProduct: OK");
    }

    @Test
    void testGetAll() {
        Product product1 = ProductFactory.createDefault();
        Product product2 = ProductFactory.createDefault();

        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<Product> products = productService.getAll();

        Assertions.assertNotNull(products);
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals(product1.getName(), products.get(0).getName());
        Assertions.assertEquals(product2.getName(), products.get(1).getName());

        System.out.println("testGetAll: OK");
    }

    @Test
    void testUpdate_ExistingProduct() {
        Product product = ProductFactory.createDefault();
        Product updated_product = ProductFactory.createDefault();
        updated_product.setName("更新名");
        updated_product.setPrice(1990);
        updated_product.setSku("change-to-1");
        updated_product.setQuantity(66);
        updated_product.setStatus(false);

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(updated_product);

        Product result_product = productService.update(product.getId(), updated_product);

        // 捕獲 productRepository.save() 時的 Product.class 內容
        ArgumentCaptor<Product> cartProductCaptor = ArgumentCaptor.forClass(Product.class);
        Mockito.verify(productRepository).save(cartProductCaptor.capture());
        Product capturedProduct = cartProductCaptor.getValue();

        Assertions.assertNotNull(result_product);
        Assertions.assertEquals(updated_product.getName(), capturedProduct.getName());
        Assertions.assertEquals(updated_product.getPrice(), capturedProduct.getPrice());
        Assertions.assertEquals(updated_product.getSku(), capturedProduct.getSku());
        Assertions.assertEquals(updated_product.getQuantity(), capturedProduct.getQuantity());
        Assertions.assertEquals(updated_product.getStatus(), capturedProduct.getStatus());

        System.out.println("testUpdate_ExistingProduct: OK");
    }

    @Test
    void testUpdate_NonExistingProduct() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Product result = productService.update(99L, ProductFactory.createDefault());

        Assertions.assertNull(result);

        // 驗證 repository 的 findById() 被使用，但 save() 未被調用
        Mockito.verify(productRepository, Mockito.times(1)).findById(99L);
        Mockito.verify(productRepository, Mockito.never()).save(any(Product.class));

        System.out.println("testUpdate_NonExistingProduct: OK");
    }
} 