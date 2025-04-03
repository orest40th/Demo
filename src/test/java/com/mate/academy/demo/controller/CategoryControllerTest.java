package com.mate.academy.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.academy.demo.dto.CreateCategoryRequestDto;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.CategoryRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void resetDatabase() throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("db.changesets/teardown.sql")
            );
        }
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("create a category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Textbook");
        requestDto.setDescription("Textbooks");

        mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        List<Category> all = categoryRepository.findAll();
        Assertions.assertEquals(1, all.size());
        assertEquals(all.get(0).getName(), requestDto.getName());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("get a category")
    void getCategoryById_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Mynewcategory");
        requestDto.setDescription("Textbooks");

        MvcResult mvcResult1 = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Category category1 = objectMapper.readValue(
                mvcResult1.getResponse().getContentAsString(),
                Category.class);

        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}", category1.getId())
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<Category> all = categoryRepository.findAll();
        Assertions.assertEquals(1, all.size());
        Category category = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Category.class);

        assertEquals(all.get(0).getName(), category.getName());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("delete a category")
    void deleteCategory_ValidId_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Textbook");
        requestDto.setDescription("Textbooks");

        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Category category = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Category.class);

        mockMvc.perform(delete("/categories/{id}", category.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        List<Category> all = categoryRepository.findAll();
        Assertions.assertTrue(all.isEmpty());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("insert and update a category")
    void updateCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName("Mynewcategory");
        requestDto.setDescription("Textbooks");

        CreateCategoryRequestDto requestDtoUpd = new CreateCategoryRequestDto();
        requestDtoUpd.setName("Updated");
        requestDtoUpd.setDescription("Updated categories");

        MvcResult mvcResult1 = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        Category category1 = objectMapper.readValue(
                mvcResult1.getResponse().getContentAsString(),
                Category.class);

        MvcResult mvcResult = mockMvc.perform(put("/categories/{id}", category1.getId())
                        .content(objectMapper.writeValueAsString(requestDtoUpd))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        Category category = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Category.class);

        assertEquals(category.getName(), requestDtoUpd.getName());
        assertEquals(category.getDescription(), requestDtoUpd.getDescription());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Get book by category")
    void getBooksByCategory_ValidId_Success() throws Exception {
        Category category = new Category();
        category.setName("Romance");
        category.setDescription("Romance");
        categoryRepository.save(category);

        Book saved = new Book();
        saved.setAuthor("AuthorA");
        saved.setTitle("Java textbook");
        saved.setCategories(Set.of(category));
        saved.setDescription("Desc desc desc");
        saved.setPrice(BigDecimal.valueOf(20));
        saved.setIsbn("9780134685991A");
        saved.setCoverImage("src/cover.png");
        bookRepository.save(saved);

        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}/books", category.getId()))
                .andExpect(status().isOk())
                .andReturn();

        Map<String, Object> pageResponse = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<Map<String, Object>>() {}
        );

        List<Book> content = objectMapper.convertValue(
                pageResponse.get("content"),
                new TypeReference<List<Book>>() {}
        );

        assertEquals(content.get(0).getIsbn(), saved.getIsbn());
    }
}
