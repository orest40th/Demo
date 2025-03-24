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
import com.mate.academy.demo.dto.BookDto;
import com.mate.academy.demo.dto.CreateBookRequestDto;
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
public class BookControllerTest {
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
    @DisplayName("create a new product")
    void createBook_ValidRequestDto_Success() throws Exception {
        Category category = new Category();
        category.setName("Textbook");
        category.setDescription("textbooks");
        categoryRepository.save(category);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Author Au");
        requestDto.setTitle("Java textbook");
        requestDto.setCategoryIds(Set.of(category.getId()));
        requestDto.setDescription("Desc desc desc");
        requestDto.setPrice(BigDecimal.valueOf(20));
        requestDto.setIsbn("9780134685991");
        requestDto.setCoverImage("src/cover.png");

        BookDto respDto = new BookDto();
        respDto.setIsbn("9780134685991");

        MvcResult mvcResult = mockMvc.perform(
                        post("/books")
                                .content(objectMapper.writeValueAsBytes(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isCreated())
                .andReturn();

        BookDto book = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );

        assertEquals(book.getIsbn(), respDto.getIsbn());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("update an existing product")
    void updateBook_ValidRequestDto_Success() throws Exception {
        Category category = new Category();
        category.setName("Romance");
        category.setDescription("Romance");
        categoryRepository.save(category);

        Book saved = new Book();
        saved.setAuthor("Author Au");
        saved.setTitle("Java textbook");
        saved.setCategories(Set.of(category));
        saved.setDescription("Desc desc desc");
        saved.setPrice(BigDecimal.valueOf(20));
        saved.setIsbn("9780134685991");
        saved.setCoverImage("src/cover.png");
        bookRepository.save(saved);

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Author Au");
        requestDto.setTitle("Java romance");
        requestDto.setCategoryIds(Set.of((category.getId())));
        requestDto.setDescription("Desc desc desc");
        requestDto.setPrice(BigDecimal.valueOf(20));
        requestDto.setIsbn("9780134685991");
        requestDto.setCoverImage("src/cover.png");

        MvcResult mvcResult = mockMvc.perform(
                        put("/books/{id}", saved.getId())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andReturn();

        BookDto book = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                BookDto.class
        );

        assertEquals(requestDto.getIsbn(), book.getIsbn());
        assertEquals(book.getCategoryIds(), requestDto.getCategoryIds());
        assertEquals(requestDto.getTitle(), book.getTitle());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("search by author - succeeds and returns 1 of 2")
    void searchAll_ValidSpec_Success() throws Exception {
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

        Book savedB = new Book();
        savedB.setAuthor("AuthorB");
        savedB.setTitle("Java textbook");
        savedB.setCategories(Set.of(category));
        savedB.setDescription("Desc desc desc");
        savedB.setPrice(BigDecimal.valueOf(20));
        savedB.setIsbn("9780134685991B");
        savedB.setCoverImage("src/cover.png");
        bookRepository.save(savedB);

        MvcResult mvcResult = mockMvc.perform(
                get("/books/search?authors=AuthorA", saved.getId()))
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

        assertEquals(1, content.size());
        assertEquals(saved.getAuthor(), content.get(0).getAuthor());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("get 2 books by respective ids - succeeds")
    void getBookById_ValidId_Success() throws Exception {
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
        saved.setIsbn("9780134685991AA");
        saved.setCoverImage("src/cover.png");
        bookRepository.save(saved);

        Book savedB = new Book();
        savedB.setAuthor("AuthorB");
        savedB.setTitle("Java textbook");
        savedB.setCategories(Set.of(category));
        savedB.setDescription("Desc desc desc");
        savedB.setPrice(BigDecimal.valueOf(20));
        savedB.setIsbn("9780134685991BB");
        savedB.setCoverImage("src/cover.png");
        bookRepository.save(savedB);

        MvcResult mvcResult = mockMvc.perform(
                        get("/books/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResult2 = mockMvc.perform(
                        get("/books/{id}", savedB.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(
                saved.getIsbn(),
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        Book.class).getIsbn()
        );

        assertEquals(
                savedB.getIsbn(),
                objectMapper.readValue(
                        mvcResult2.getResponse().getContentAsString(),
                        Book.class).getIsbn()
        );
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("delete book and try to find it - fails")
    void deleteAndRetrieveBookBook_ValidId_Failure() throws Exception {
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
        saved.setIsbn("9780134685991AAA");
        saved.setCoverImage("src/cover.png");
        bookRepository.save(saved);

        mockMvc.perform(
                delete("/books/{id}", saved.getId()))
                .andExpect(status().isNoContent())
                .andReturn();

        MvcResult mvcResult2 = mockMvc.perform(
                        get("/books/{id}", saved.getId()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("")
    void getAllBooks_NonnullNumber_True() throws Exception {
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
        saved.setIsbn("9780134685991AAAA");
        saved.setCoverImage("src/cover.png");
        bookRepository.save(saved);

        MvcResult mvcResult = mockMvc.perform(
                        get("/books"))
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

        Assertions.assertTrue(!content.isEmpty());
    }
}
