package com.example.api.repository;

import com.example.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRole(String role);

    @Query("SELECT r from Role where r.name=?1")
    Role findByName(String name);
}
