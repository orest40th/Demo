package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Order;

import java.util.Optional;

import com.mate.academy.demo.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"user", "user.roles", "orderItems", "orderItems.book"})
    Page<Order> findAllByUserId(Pageable pageable, Long userId);

    @Query(value = "from Order o join fetch o.user u where u.id = :userId and o.id = :orderId")
    Optional<Order> findByOrderId(Long userId, Long orderId);
}
