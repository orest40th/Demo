package com.mate.academy.demo.repository;

import com.mate.academy.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "from Role where roleName = :roleName")
    Role findByName(Role.RoleName roleName);
}
