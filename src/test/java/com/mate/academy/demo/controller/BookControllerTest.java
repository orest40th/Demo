package com.mate.academy.demo.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mate.academy.demo.dto.CreateBookRequestDto;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.CategoryRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void setup() {
        Category category2 = new Category();
        category2.setName("2Romance");
        category2.setDescription("2Romance");
        categoryRepository.save(category2);
        System.out.println(bookRepository.findAll());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("create a new product")
    void createBook_ValidRequestDto_Success() throws Exception {
        Category category = new Category();
        category.setName("Romance");
        category.setDescription("Romance");

        categoryRepository.save(category);

        System.out.println(bookRepository.findAll() + "BOOKS DOUFOUND");
        System.out.println(categoryRepository.findAll() + "CATS FAOUND");
        categoryRepository.findAll().forEach(cat -> System.out.println(cat.getId()));

        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setAuthor("Author Au");
        requestDto.setTitle("Java textbook");
        Set<Long> cats = new HashSet<>();
        cats.add(1L);
        requestDto.setCategoryIds(cats);
        requestDto.setDescription("Desc desc desc");
        requestDto.setPrice(BigDecimal.valueOf(20));
        requestDto.setIsbn("9780134685991");
        requestDto.setCoverImage("src/cover.png");

        /*
        * BookDto respDto = new BookDto();
        respDto.setAuthor("Authora");
        respDto.setTitle("Java");
        respDto.setCategoryIds(null);
        respDto.setDescription("desc");
        respDto.setPrice(BigDecimal.ONE);
        respDto.setIsbn("9783161484100");
        respDto.setCoverImage("cover/img.png");
        * */

        mockMvc.perform(
                post("/books")
                        .content(objectMapper.writeValueAsBytes(requestDto))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isCreated())
                .andReturn();

        Optional<Book> createdBook = bookRepository.findById(1L);
        assertNotNull(createdBook.get(), "Book should be created");
    }
}
