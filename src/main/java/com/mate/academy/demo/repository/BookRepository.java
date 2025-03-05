package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "Select bk from Book bk join fetch bk.categories cat where cat.id = :id")
    List<Book> findAllByCategoryId(@Param("id") Long categoryId);
}
