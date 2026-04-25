package com.shopsphere.catalog.controller;

import com.shopsphere.catalog.dto.CategoryDto;
import com.shopsphere.catalog.dto.ProductDto;
import com.shopsphere.catalog.service.CategoryService;
import com.shopsphere.catalog.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category browsing and category-scoped product listing")
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/categories")
    @Operation(summary = "List all categories")
    public ResponseEntity<List<CategoryDto>> listCategories() {
        return ResponseEntity.ok(categoryService.listAll());
    }

    @GetMapping("/categories/{id}")
    @Operation(summary = "List active products inside a given category")
    public ResponseEntity<Page<ProductDto>> productsInCategory(@PathVariable Long id,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "12") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.productsInCategory(id, pageable));
    }

    @GetMapping("/featured")
    @Operation(summary = "List featured products (homepage carousel)")
    public ResponseEntity<List<ProductDto>> featured() {
        return ResponseEntity.ok(productService.listFeatured());
    }
}
