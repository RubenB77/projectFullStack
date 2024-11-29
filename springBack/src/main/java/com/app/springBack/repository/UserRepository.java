package com.app.springBack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.springBack.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
}
