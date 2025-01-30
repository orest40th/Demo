package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Book;
import java.util.List;
import java.util.Map;

public interface BookRepositoryCustom {
    List<Book> findAll(Map<String, List<String>> params);
}
