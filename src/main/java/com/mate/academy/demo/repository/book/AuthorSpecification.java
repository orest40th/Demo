package com.mate.academy.demo.repository.book;

import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.model.Book;
import com.mate.academy.demo.repository.SpecificationProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecification implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecification(String[] params) {
        return new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return root.get("author").in(Arrays.stream(params).toArray());
            }
        };
    }

    @Override
    public boolean isApplicable(BookSearchParameters.FieldTypes type) {
        return type.equals(BookSearchParameters.FieldTypes.AUTHORS);
    }
}
