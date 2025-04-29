package com.shopjava.shopjava_forestage_backend.service;

import com.shopjava.shopjava_forestage_backend.model.Product;
import com.shopjava.shopjava_forestage_backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Product updatedProduct;

    @BeforeEach
    void setUp() {
        // 創建測試用的產品對象
        product = new Product();
        product.setId(1L);
        product.setName("測試產品");
        product.setPrice(1000);
        product.setSku("TST-001");
        product.setRoute("test-product");
        product.setDescription("這是一個測試產品");
        product.setQuantity(100);
        product.setStatus(true);
        product.setFirstPhoto("photo.jpg");
        
        // 創建更新後的產品對象
        updatedProduct = new Product();
        updatedProduct.setName("更新後的產品");
        updatedProduct.setPrice(2000);
        updatedProduct.setSku("TST-002");
        updatedProduct.setRoute("updated-product");
        updatedProduct.setDescription("這是更新後的產品描述");
        updatedProduct.setQuantity(200);
        updatedProduct.setStatus(false);
        updatedProduct.setFirstPhoto("updated-photo.jpg");
    }

    @Test
    void testCreate() {
        // 配置模擬行為
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // 調用被測試的方法
        Product savedProduct = productService.create(product);

        // 驗證結果
        assertNotNull(savedProduct);
        assertEquals("測試產品", savedProduct.getName());
        assertEquals(1000, savedProduct.getPrice());
        assertEquals("TST-001", savedProduct.getSku());
        
        // 驗證repository的save方法被調用
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDelete() {
        // 調用被測試的方法
        productService.delete(1L);

        // 驗證repository的deleteById方法被調用
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetById_ExistingProduct() {
        // 配置模擬行為
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // 調用被測試的方法
        Optional<Product> foundProduct = productService.getById(1L);

        // 驗證結果
        assertTrue(foundProduct.isPresent());
        assertEquals("測試產品", foundProduct.get().getName());
        
        // 驗證repository的findById方法被調用
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetById_NonExistingProduct() {
        // 配置模擬行為
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // 調用被測試的方法
        Optional<Product> foundProduct = productService.getById(99L);

        // 驗證結果
        assertFalse(foundProduct.isPresent());
        
        // 驗證repository的findById方法被調用
        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void testGetAll() {
        // 創建第二個產品用於測試
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("測試產品2");
        product2.setPrice(500);
        
        // 配置模擬行為
        when(productRepository.findAll()).thenReturn(Arrays.asList(product, product2));

        // 調用被測試的方法
        List<Product> products = productService.getAll();

        // 驗證結果
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("測試產品", products.get(0).getName());
        assertEquals("測試產品2", products.get(1).getName());
        
        // 驗證repository的findAll方法被調用
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testUpdate_ExistingProduct() {
        // 配置模擬行為
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // 調用被測試的方法
        Product result = productService.update(1L, updatedProduct);

        // 驗證結果
        assertNotNull(result);
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getSku(), result.getSku());
        assertEquals(updatedProduct.getRoute(), result.getRoute());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        assertEquals(updatedProduct.getQuantity(), result.getQuantity());
        assertEquals(updatedProduct.getStatus(), result.getStatus());
        assertEquals(updatedProduct.getFirstPhoto(), result.getFirstPhoto());
        
        // 驗證repository的方法被調用
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdate_NonExistingProduct() {
        // 配置模擬行為
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // 調用被測試的方法
        Product result = productService.update(99L, updatedProduct);

        // 驗證結果
        assertNull(result);
        
        // 驗證repository的findById方法被調用，但save方法未被調用
        verify(productRepository, times(1)).findById(99L);
        verify(productRepository, never()).save(any(Product.class));
    }
} 