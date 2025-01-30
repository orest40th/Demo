package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryCustomImpl implements BookRepositoryCustom {
    @Autowired
    private EntityManager manager;

    @Override
    public List<Book> findAll(Map<String, List<String>> params) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        Root<Book> root = query.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            predicates.add((root.get(entry.getKey()).in(entry.getValue())));
        }

        query.select(root).where(cb.and(predicates.toArray(new Predicate[]{})));
        return manager.createQuery(query).getResultList();
    }
}
