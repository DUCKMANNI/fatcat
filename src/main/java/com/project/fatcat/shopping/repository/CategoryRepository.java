package com.project.fatcat.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findAllByOrderByMainCodeAscSubCodeAsc();
}
