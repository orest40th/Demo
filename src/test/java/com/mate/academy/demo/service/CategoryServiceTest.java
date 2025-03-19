package com.mate.academy.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mate.academy.demo.dto.CategoryDto;
import com.mate.academy.demo.dto.CreateCategoryRequestDto;
import com.mate.academy.demo.mapper.impl.CategoryMapperImpl;
import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryMapperImpl mapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void findAll_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(new Category(), new Category());

        Mockito.when(repository.findAll(pageable)).thenReturn(new PageImpl<>(categories));
        Mockito.when(mapper.toDto(Mockito.any(Category.class)))
                .thenAnswer(invocation -> {
                    Category category = invocation.getArgument(0);
                    return new CategoryDto();
                });

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getById_ExistingId_Success() {
        Long id = 1L;
        Category category = new Category();
        category.setName("Category1");
        category.setId(id);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(id);
        categoryDto.setName("Category1");

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(category));
        Mockito.when(mapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.getById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Category1", result.getName());
    }

    @Test
    void save_Success() {
        Category category = new Category();
        category.setName("cat1");
        category.setId(1L);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("cat1");
        categoryDto.setId(1L);
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        Mockito.when(mapper.toEntity(requestDto)).thenReturn(category);
        Mockito.when(repository.save(category)).thenReturn(category);
        Mockito.when(mapper.toDto(category)).thenReturn(categoryDto);

        CategoryDto result = categoryService.save(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("cat1", result.getName());
    }

    @Test
    void update_ExistingId_Success() {
        Category category = new Category();
        category.setId(1L);
        category.setDescription("Old");
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("UpdatedCategory");
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(category));
        Mockito.doNothing().when(mapper).updateCategoryFromDto(requestDto, category);
        Mockito.when(repository.save(category)).thenReturn(category);
        Mockito.when(mapper.toDto(category)).thenReturn(categoryDto);
        CategoryDto result = categoryService.update(1L, requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("UpdatedCategory", result.getName());
    }

    @Test
    void deleteById_Success() {
        Long id = 1L;
        Mockito.doNothing().when(repository).deleteById(id);
        assertDoesNotThrow(() -> categoryService.deleteById(id));
        Mockito.verify(repository, Mockito.times(1)).deleteById(id);
    }
}
