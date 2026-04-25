package com.shopsphere.catalog.service;

import com.shopsphere.catalog.dto.CategoryDto;
import com.shopsphere.catalog.entity.Category;
import com.shopsphere.catalog.exception.ResourceNotFoundException;
import com.shopsphere.catalog.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> listAll() {
        return categoryRepository.findAll().stream().map(CategoryDto::from).toList();
    }

    public CategoryDto getById(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        return CategoryDto.from(c);
    }
}
