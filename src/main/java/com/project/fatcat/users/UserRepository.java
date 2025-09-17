package com.project.fatcat.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
