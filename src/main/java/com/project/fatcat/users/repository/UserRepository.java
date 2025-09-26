package com.project.fatcat.users.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserEmail(String userEmail);
}
