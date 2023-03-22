package com.example.aranoz.services;

import com.example.aranoz.domain.entities.User;
import com.example.aranoz.domain.entities.UserRole;
import com.example.aranoz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return map(userRepository.findFirstByEmailEquals(email));
    }

    private UserDetails map(User user){
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),extractAuthorities(user));
    }

    private List<GrantedAuthority> extractAuthorities(User user){
        return user.getRole().stream().map(this::mapAuthority).toList();
    }

    private GrantedAuthority mapAuthority(UserRole userRole){
        return new SimpleGrantedAuthority("ROLE_" + userRole.getRole().name());
    }
}
