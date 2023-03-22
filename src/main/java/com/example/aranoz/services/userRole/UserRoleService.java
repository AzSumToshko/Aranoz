package com.example.aranoz.services.userRole;

import com.example.aranoz.domain.dtoS.view.User.UserRoleVM;
import com.example.aranoz.domain.entities.UserRole;
import com.example.aranoz.services.init.DataBaseInitService;

import java.util.List;

public interface UserRoleService extends DataBaseInitService {
    List<UserRoleVM> getAll();
    UserRole getUserRole();
    UserRole getAdminRole();
}
