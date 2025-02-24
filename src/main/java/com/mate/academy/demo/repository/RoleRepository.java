package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT * FROM roles WHERE role_name = :name", nativeQuery = true)
    Optional<Role> findByName(@Param("name") String roleName);
}
