package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("from ShoppingCart sc left outer join fetch sc.cartItems where sc.user.email = :email")
    Optional<ShoppingCart> findByUserEmail(@Param("email") String email);
}
