package com.mate.academy.demo.service;

import com.mate.academy.demo.dto.CategoryDto;
import com.mate.academy.demo.dto.CreateCategoryRequestDto;
import com.mate.academy.demo.mapper.CategoryMapper;
import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .getContent()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Category not found by id " + id));
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        return mapper.toDto(repository.save(mapper.toEntity(categoryDto)));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category catFetched = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found by id " + id));
        mapper.updateCategoryFromDto(categoryDto, catFetched);
        return mapper.toDto(repository.save(catFetched));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
