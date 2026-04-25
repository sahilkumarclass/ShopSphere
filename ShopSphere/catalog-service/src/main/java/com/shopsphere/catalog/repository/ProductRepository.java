package com.shopsphere.catalog.repository;

import com.shopsphere.catalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByIsFeaturedTrueAndIsActiveTrue();
    Page<Product> findByCategoryIdAndIsActiveTrue(Long categoryId, Pageable pageable);
}
