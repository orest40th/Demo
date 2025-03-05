package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CategoryDto;
import com.mate.academy.demo.model.Category;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<Category> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
