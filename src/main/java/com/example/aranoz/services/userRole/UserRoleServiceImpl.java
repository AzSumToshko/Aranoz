package com.example.aranoz.services.userRole;

import com.example.aranoz.domain.dtoS.view.User.UserRoleVM;
import com.example.aranoz.domain.entities.UserRole;
import com.example.aranoz.domain.enums.Role;
import com.example.aranoz.repository.UserRoleRepository;
import com.example.aranoz.services.init.DataBaseInitService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService, DataBaseInitService {
    private final UserRoleRepository userRoleRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, ModelMapper modelMapper) {
        this.userRoleRepository = userRoleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void dbInit() {
        List<UserRole> roles = new ArrayList<>();
        roles.add(new UserRole().setRole(Role.USER));
        roles.add(new UserRole().setRole(Role.ADMIN));

        this.userRoleRepository.saveAllAndFlush(roles);
    }

    @Override
    public boolean isDbInit() {
        return this.userRoleRepository.count() > 0;
    }

    public List<UserRoleVM> getAll(){//return all roles
        return this.userRoleRepository.findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, UserRoleVM.class))
                .toList();
    }

    @Override
    public UserRole getUserRole() {
        return this.userRoleRepository.findByRole(Role.USER);
    }

    @Override
    public UserRole getAdminRole() {
        return this.userRoleRepository.findByRole(Role.ADMIN);
    }
}
