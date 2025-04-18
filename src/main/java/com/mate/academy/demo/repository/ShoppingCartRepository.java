package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book", "user"})
    Optional<ShoppingCart> findByUserId(Long id);
}
