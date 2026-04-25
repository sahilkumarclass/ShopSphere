package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.ProductDto;
import com.shopsphere.catalog.entity.Category;
import com.shopsphere.catalog.entity.Product;
import com.shopsphere.catalog.exception.ResourceNotFoundException;
import com.shopsphere.catalog.repository.CategoryRepository;
import com.shopsphere.catalog.repository.ProductRepository;
import com.shopsphere.catalog.spec.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductDto> listProducts(String search, Long categoryId,
                                         BigDecimal minPrice, BigDecimal maxPrice,
                                         Pageable pageable) {
        return productRepository
                .findAll(ProductSpecification.withFilters(search, categoryId, minPrice, maxPrice), pageable)
                .map(ProductDto::from);
    }

    public List<ProductDto> listFeatured() {
        return productRepository.findByIsFeaturedTrueAndIsActiveTrue()
                .stream().map(ProductDto::from).toList();
    }

    public Page<ProductDto> productsInCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
                .map(ProductDto::from);
    }

    public ProductDto getById(Long id) {
        return ProductDto.from(loadProduct(id));
    }

    @Transactional
    public ProductDto create(ProductDto dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .discountPct(dto.getDiscountPct() == null ? BigDecimal.ZERO : dto.getDiscountPct())
                .stockQty(dto.getStockQty())
                .imageUrl(dto.getImageUrl())
                .category(dto.getCategoryId() == null ? null : loadCategory(dto.getCategoryId()))
                .isFeatured(Boolean.TRUE.equals(dto.getIsFeatured()))
                .isActive(true)
                .build();
        return ProductDto.from(productRepository.save(product));
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        Product product = loadProduct(id);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setDiscountPct(dto.getDiscountPct() == null ? BigDecimal.ZERO : dto.getDiscountPct());
        product.setStockQty(dto.getStockQty());
        product.setImageUrl(dto.getImageUrl());
        product.setIsFeatured(Boolean.TRUE.equals(dto.getIsFeatured()));
        if (dto.getCategoryId() != null) {
            product.setCategory(loadCategory(dto.getCategoryId()));
        }
        return ProductDto.from(product);
    }

    @Transactional
    public void delete(Long id) {
        Product product = loadProduct(id);
        product.setIsActive(false);
    }

    @Transactional
    public ProductDto updateStock(Long id, Integer stockQty) {
        Product product = loadProduct(id);
        product.setStockQty(stockQty);
        return ProductDto.from(product);
    }

    private Product loadProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    private Category loadCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }
}
