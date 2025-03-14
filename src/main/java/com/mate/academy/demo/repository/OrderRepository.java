package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user", "user.roles", "orderItems", "orderItems.book"})
    Page<Order> findAllByUserId(Pageable pageable, Long userId);

    @EntityGraph(attributePaths = {"user", "user.roles", "orderItems", "orderItems.book"})
    List<Order> findListAllByUserId(Pageable pageable, Long userId);
}
