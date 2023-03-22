package com.example.aranoz.services.user;

import com.example.aranoz.domain.dtoS.model.User.*;
import com.example.aranoz.domain.entities.User;
import com.example.aranoz.services.init.DataBaseInitService;

import java.util.List;

public interface UserService extends DataBaseInitService {
    void registerUser(UserRegisterFormDto model) throws Exception;
    void createUser(CreateUserFormDto model) throws Exception;
    void removeById(String id);
    void updateUser(UpdateUserFormDto model) throws Exception;
    User findById(String id);
    List<User> getAllUsers();
    void editUserProfile(UserProfileEditDto model) throws Exception;
    User findByEmail(String email);
    void changePassword(UserChangePasswordFormDto model) throws Exception;
    User authenticateUser(UserLoginFormDto userLoginFormDto) throws Exception;
    User getUserByEmail(String name);
}
