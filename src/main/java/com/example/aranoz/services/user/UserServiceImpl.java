package com.example.aranoz.services.user;

import com.example.aranoz.domain.dtoS.model.User.*;
import com.example.aranoz.domain.entities.User;
import com.example.aranoz.domain.entities.UserRole;
import com.example.aranoz.domain.enums.Role;
import com.example.aranoz.repository.UserRepository;
import com.example.aranoz.services.UserDetailsService;
import com.example.aranoz.services.init.DataBaseInitService;
import com.example.aranoz.services.userRole.UserRoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.aranoz.constants.Constants.*;
import static com.example.aranoz.constants.Errors.*;

@Service
public class UserServiceImpl implements UserService, DataBaseInitService {
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserRoleService userRoleService, ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void dbInit() {
        User user = new User();
        user.setFirstName(DEFAULT_USER_FIRST_NAME);
        user.setLastName(DEFAULT_USER_LAST_NAME);
        user.setEmail(DEFAULT_USER_EMAIL_ADDRESS);
        user.setPassword(this.passwordEncoder.encode(DEFAULT_USER_PASSWORD));

        List<UserRole> roles = new ArrayList<>();
        roles.add(this.userRoleService.getAdminRole());
        roles.add(this.userRoleService.getUserRole());

        user.setRole(roles);
        user.setCreated(new Date());
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public boolean isDbInit() {
        return this.userRepository.count() > 0;
    }

    public boolean emailExists(String email){
        if (userRepository.findFirstByEmailEquals(email) != null){
            return true;
        }else{
            return false;
        }
    }
    @Override
    public void registerUser(UserRegisterFormDto model) throws Exception {
        if (emailExists(model.getEmail())){
            throw new Exception(String.format(USER_EMAIL_TAKEN_FORMAT,model.getEmail()));
        }


        User userToInsert = modelMapper.map(model,User.class);
        userToInsert.setPassword(passwordEncoder.encode(userToInsert.getPassword()));

        List<UserRole> roles = new ArrayList<>();
        roles.add(this.userRoleService.getUserRole());
        userToInsert.setRole(roles);
        userToInsert.setCreated(new Date());
        userRepository.saveAndFlush(userToInsert);
    }

    @Override
    public void createUser(CreateUserFormDto model) throws Exception {
        if (emailExists(model.getEmail())){
            throw new Exception(String.format(USER_EMAIL_TAKEN_FORMAT,model.getEmail()));
        }

        User userToInsert = modelMapper.map(model,User.class);
        userToInsert.setPassword(passwordEncoder.encode(userToInsert.getPassword()));
        userToInsert.setCreated(new Date());

        setUserRoles(model.getRole(), userToInsert);
        userRepository.saveAndFlush(userToInsert);
    }

    @Override
    public void removeById(String id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public void updateUser(UpdateUserFormDto model) throws Exception {
        if (!this.userRepository.findFirstByEmailEquals(model.getEmail()).getId().equals(model.getId())){
            throw new Exception(String.format(USER_EMAIL_TAKEN_FORMAT,model.getEmail()));
        }

        User userToInsert = this.modelMapper.map(model,User.class);

        if (!model.getPassword().isBlank()){
            if (!model.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")){
                throw new Exception(USER_INVALID_PASSWORD);
            }
            userToInsert.setPassword(this.passwordEncoder.encode(model.getPassword()));
        }else{
            userToInsert.setPassword(this.userRepository.findById(model.getId()).get().getPassword());
        }

        userToInsert.setCreated(this.userRepository.findById(userToInsert.getId()).get().getCreated());

        setUserRoles(model.getRole(), userToInsert);
        userRepository.saveAndFlush(userToInsert);
    }

    private void setUserRoles(Role role, User userToInsert) {
        List<UserRole> roles = new ArrayList<>();

        if (role.equals(Role.ADMIN)){
            roles.add(this.userRoleService.getAdminRole());
        }

        roles.add(this.userRoleService.getUserRole());
        userToInsert.setRole(roles);
    }

    @Override
    public User findById(String id) {
        return this.userRepository.findById(id).get();
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public void editUserProfile(UserProfileEditDto model) throws Exception {
        if (!this.userRepository.findFirstByEmailEquals(model.getEmail()).getId().equals(model.getId())){
            throw new Exception(String.format(USER_EMAIL_TAKEN_FORMAT,model.getEmail()));
        }

        User userToInsert = this.modelMapper.map(model,User.class);

        userToInsert.setCreated(this.userRepository.findById(userToInsert.getId()).get().getCreated());
        userToInsert.setRole(this.userRepository.findById(userToInsert.getId()).get().getRole());
        userToInsert.setPassword(this.userRepository.findById(userToInsert.getId()).get().getPassword());

        userRepository.saveAndFlush(userToInsert);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findFirstByEmailEquals(email);
    }

    @Override
    public void changePassword(UserChangePasswordFormDto model) throws Exception {
        if (!model.getPassword().equals(model.getRePassword())){
            throw new Exception(USER_PASSWORDS_NOT_MATCHING);
        }

        User tempUser = findById(model.getId());

        tempUser.setPassword(this.passwordEncoder.encode(model.getPassword()));
        this.userRepository.saveAndFlush(tempUser);
    }

    @Override
    public User authenticateUser(UserLoginFormDto userLoginFormDto) throws Exception {
        User user = this.userRepository.findFirstByEmailEquals(userLoginFormDto.getEmail());

        if (user == null || !passwordEncoder.matches(userLoginFormDto.getPassword(), user.getPassword())){
            throw new Exception(USER_NOT_FOUND);
        }

        UserDetails user2 = userDetailsService.loadUserByUsername(user.getEmail());

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findFirstByEmailEquals(email);
    }
}