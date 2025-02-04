package com.mate.academy.demo.repository;

import com.mate.academy.demo.dto.BookSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    Specification<T> getSpecification(String[] params);

    boolean isApplicable(BookSearchParameters.FieldTypes type);
}
