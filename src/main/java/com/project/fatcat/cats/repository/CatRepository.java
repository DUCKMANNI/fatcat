package com.project.fatcat.cats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Cat;

public interface CatRepository extends JpaRepository<Cat, Integer> {

}
