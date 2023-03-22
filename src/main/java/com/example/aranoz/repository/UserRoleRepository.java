package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.UserRole;
import com.example.aranoz.domain.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    UserRole findByRole(Role role);
}
