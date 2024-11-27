package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
