package com.mate.academy.demo.dao;

import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    BookRepository repository;

    @Test
    @DisplayName("add 2 books and manage to retrieve 2 products from db")
    void saveAndFindAll_TwoProducts_True() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        repository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        repository.save(book2);

        Page<Book> books = repository.findAll(PageRequest.of(0, 10));

        assertNotNull(books);
        assertEquals(2, books.getTotalElements());
    }
}
