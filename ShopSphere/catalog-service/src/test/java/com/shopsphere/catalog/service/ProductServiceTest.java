package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.ProductDto;
import com.shopsphere.catalog.entity.Category;
import com.shopsphere.catalog.entity.Product;
import com.shopsphere.catalog.exception.ResourceNotFoundException;
import com.shopsphere.catalog.repository.CategoryRepository;
import com.shopsphere.catalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private ProductService productService;

    @Test
    void getById_returnsProduct_whenExists() {
        Category cat = Category.builder().id(1L).name("Electronics").build();
        Product p = Product.builder()
                .id(1L).name("Laptop").price(new BigDecimal("999.99"))
                .stockQty(5).category(cat).isActive(true).discountPct(BigDecimal.ZERO)
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        ProductDto dto = productService.getById(1L);

        assertEquals("Laptop", dto.getName());
        assertEquals(new BigDecimal("999.99"), dto.getPrice());
        assertEquals("Electronics", dto.getCategoryName());
    }

    @Test
    void getById_throwsNotFound_whenMissing() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getById(99L));
    }

    @Test
    void create_savesProductWithCategory() {
        Category cat = Category.builder().id(2L).name("Apparel").build();
        ProductDto dto = ProductDto.builder()
                .name("Shoes").price(new BigDecimal("100"))
                .stockQty(10).categoryId(2L).build();

        when(categoryRepository.findById(2L)).thenReturn(Optional.of(cat));
        when(productRepository.save(any(Product.class)))
                .thenAnswer(inv -> {
                    Product saved = inv.getArgument(0);
                    saved.setId(42L);
                    return saved;
                });

        ProductDto result = productService.create(dto);

        assertEquals(42L, result.getId());
        assertEquals("Apparel", result.getCategoryName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void delete_marksProductInactive() {
        Product p = Product.builder().id(7L).isActive(true).discountPct(BigDecimal.ZERO).build();
        when(productRepository.findById(7L)).thenReturn(Optional.of(p));

        productService.delete(7L);

        assertFalse(p.getIsActive());
    }

    @Test
    void updateStock_changesStockQty() {
        Product p = Product.builder().id(3L).stockQty(5).discountPct(BigDecimal.ZERO).build();
        when(productRepository.findById(3L)).thenReturn(Optional.of(p));

        ProductDto dto = productService.updateStock(3L, 25);

        assertEquals(25, dto.getStockQty());
    }
}
