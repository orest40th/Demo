package com.mate.academy.demo.repository;

import com.mate.academy.demo.dto.BookSearchParameters;
import com.mate.academy.demo.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderStrategy<Book> strategy;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> specification = Specification.where(null);

        if (searchParameters.titles() != null && searchParameters.titles().length > 0) {
            specification = specification
                    .and(strategy.getSpecificationProvider(BookSearchParameters.FieldTypes.TITLES)
                            .getSpecification(searchParameters.titles()));
        }

        if (searchParameters.authors() != null && searchParameters.authors().length > 0) {
            specification = specification
                    .and(strategy.getSpecificationProvider(BookSearchParameters.FieldTypes.AUTHORS)
                            .getSpecification(searchParameters.authors()));
        }

        if (searchParameters.isbns() != null && searchParameters.isbns().length > 0) {
            specification = specification
                    .and(strategy.getSpecificationProvider(BookSearchParameters.FieldTypes.ISBNS)
                            .getSpecification(searchParameters.isbns()));
        }

        return specification;
    }
}
