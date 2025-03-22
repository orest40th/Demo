package com.mate.academy.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Save category and retrieve it by ID")
    void saveAndFindById_ValidId_ReturnsCategory() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Books related to technology");
        Category savedCategory = categoryRepository.save(category);

        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(foundCategory.isPresent());
        assertEquals("Tech", foundCategory.get().getName());
    }

    @Test
    @DisplayName("Find all categories after saving multiple categories")
    void findAll_AfterSavingMultipleCategories_ReturnsCorrectCount() {
        Category category1 = new Category();
        category1.setName("Tech");
        category1.setDescription("Books related to technology");

        Category category2 = new Category();
        category2.setName("Science");
        category2.setDescription("Books about science");

        categoryRepository.saveAll(List.of(category1, category2));
        List<Category> categories = categoryRepository.findAll();

        assertEquals(2, categories.size());
    }

    @Test
    @DisplayName("Delete category and ensure it no longer exists")
    void deleteCategory_AfterSaving_EnsuresItIsRemoved() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Books related to technology");
        Category savedCategory = categoryRepository.save(category);

        categoryRepository.deleteById(savedCategory.getId());
        Optional<Category> foundCategory = categoryRepository.findById(savedCategory.getId());

        assertFalse(foundCategory.isPresent());
    }
}
