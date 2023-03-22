package com.example.aranoz.domain.dtoS.view.User;

import com.example.aranoz.domain.enums.Role;
import lombok.Getter;

@Getter
public class UserRoleVM {
    private Role role;

    public UserRoleVM setRole(Role role){
        this.role = role;
        return this;
    }
}
