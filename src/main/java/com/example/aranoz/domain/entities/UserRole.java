package com.example.aranoz.domain.entities;

import com.example.aranoz.domain.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "roles")
@Getter
public class UserRole extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private Role role;

    public UserRole setRole(Role role) {
        this.role = role;
        return this;
    }
}
