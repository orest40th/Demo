package com.mate.academy.demo.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.model.Category;
import com.mate.academy.demo.repository.BookRepository;
import com.mate.academy.demo.repository.CategoryRepository;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class BookRepositoryTest {
    @Autowired
    private BookRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DataSource dataSource;

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

    @Test
    @DisplayName("add 2 books and manage to retrieve 2 products from db")
    void saveAndFindAll_TwoProducts_True() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("author1");
        book1.setCategories(Set.of());
        book1.setIsbn("123123123123");
        book1.setCoverImage("coverimg1");
        book1.setPrice(BigDecimal.ONE);
        book1.setDescription("How to tackle Docker issues that won't solve themselves");
        repository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("orest");
        book2.setCategories(Set.of());
        book2.setIsbn("43434343434");
        book2.setCoverImage("coverimg2");
        book2.setPrice(BigDecimal.TEN);
        book2.setDescription("Married to docker test containers? Easy divorce!");
        repository.save(book2);

        Page<Book> books = repository.findAll(PageRequest.of(0, 10));

        assertNotNull(books);
        assertEquals(2, books.getTotalElements());
    }

    @Test
    @DisplayName("find books by category ID")
    void findAllByCategoryId_ValidCategoryId_ReturnsBooks() {
        Category category = new Category();
        category.setName("Tech");
        category.setDescription("textbooks");
        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Docker Guide");
        book.setAuthor("Expert");
        Set<Category> cats = new HashSet<>();
        cats.add(category);
        book.setCategories(cats);
        book.setIsbn("111222333444");
        book.setCoverImage("docker-cover");
        book.setPrice(BigDecimal.valueOf(20));
        book.setDescription("All about Docker");
        repository.save(book);

        Page<Book> books = repository.findAllByCategoryId(category.getId(), PageRequest.of(0, 10));

        assertNotNull(books);
        assertEquals(1, books.getTotalElements());
        assertEquals("Docker Guide", books.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("find all books with specification")
    void findAll_WithSpecification_ReturnsFilteredBooks() {
        Book book1 = new Book();
        book1.setTitle("Java Basics");
        book1.setAuthor("Author A");
        book1.setCategories(Set.of());
        book1.setIsbn("123456789");
        book1.setCoverImage("java-cover");
        book1.setPrice(BigDecimal.valueOf(25));
        book1.setDescription("Java fundamentals");
        repository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Advanced Java");
        book2.setAuthor("Author B");
        book2.setCategories(Set.of());
        book2.setIsbn("987654321");
        book2.setCoverImage("java-advanced-cover");
        book2.setPrice(BigDecimal.valueOf(50));
        book2.setDescription("Deep dive into Java");
        repository.save(book2);

        Specification<Book> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "Java%");

        Page<Book> books = repository.findAll(spec, PageRequest.of(0, 10));

        assertNotNull(books);
        assertEquals(1, books.getTotalElements());
    }

    @Test
    @DisplayName("find book by ID")
    void findById_ExistingId_ReturnsBook() {
        Book book = new Book();
        book.setTitle("Spring Boot in Action");
        book.setAuthor("authora");
        book.setCategories(Set.of());
        book.setIsbn("987654321");
        book.setCoverImage("spring-cover");
        book.setPrice(BigDecimal.valueOf(30));
        book.setDescription("Spring Boot txtbook");
        repository.save(book);

        Optional<Book> foundBook = repository.findById(book.getId());

        assertTrue(foundBook.isPresent());
        assertEquals("Spring Boot in Action", foundBook.get().getTitle());
    }
}
