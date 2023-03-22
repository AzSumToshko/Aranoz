package com.example.aranoz.repository;

import com.example.aranoz.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findFirstByEmailEquals(String email);
    User findFirstByEmailAndAndPassword(String email, String password);
}
