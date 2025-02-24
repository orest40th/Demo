package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query(value = "from User JOIN FETCH roles where email = :email")
    Optional<User> findByEmail(String email);
}
