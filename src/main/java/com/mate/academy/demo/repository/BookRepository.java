package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Query(value = "Select bk from Book bk join fetch bk.categories cat where cat.id = :id")
    List<Book> findAllByCategoryId(@Param("id") Long categoryId, Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    @Override
    Optional<Book> findById(Long id);

    @EntityGraph(attributePaths = "categories")
    @Override
    Page<Book> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    @Override
    Page<Book> findAll(@Nullable Specification<Book> spec, Pageable pageable);
}
