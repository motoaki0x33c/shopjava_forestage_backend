package com.shopjava.shopjava_forestage_backend.unit.service;

import com.shopjava.shopjava_forestage_backend.service.ProductService;
import com.shopjava.shopjava_forestage_backend.unit.factory.ProductFactory;
import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product);

        Product savedProduct = productService.create(product);

        Assertions.assertNotNull(savedProduct);
        Assertions.assertEquals(product.getName(), savedProduct.getName());
        Assertions.assertEquals(product.getPrice(), savedProduct.getPrice());
        Assertions.assertEquals(product.getSku(), savedProduct.getSku());
        Assertions.assertEquals(product.getRoute(), savedProduct.getRoute());
        Assertions.assertEquals(product.getQuantity(), savedProduct.getQuantity());
        
        System.out.println("testCreate: OK");
    }

    @Test
    void testGetById_ExistingProduct() {
        Product product = ProductFactory.createDefault();

        Mockito.when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        Optional<Product> foundProduct = productService.getById(product.getId());

        Assertions.assertTrue(foundProduct.isPresent());
        Assertions.assertEquals(product.getName(), foundProduct.get().getName());

        System.out.println("testGetById_ExistingProduct: OK");
    }

    @Test
    void testGetById_NonExistingProduct() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> foundProduct = productService.getById(99L);

        Assertions.assertFalse(foundProduct.isPresent());

        System.out.println("testGetById_NonExistingProduct: OK");
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

        Assertions.assertNotNull(result_product);
        Assertions.assertEquals(updated_product.getName(), result_product.getName());
        Assertions.assertEquals(updated_product.getPrice(), result_product.getPrice());
        Assertions.assertEquals(updated_product.getSku(), result_product.getSku());
        Assertions.assertEquals(updated_product.getQuantity(), result_product.getQuantity());
        Assertions.assertEquals(updated_product.getStatus(), result_product.getStatus());

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