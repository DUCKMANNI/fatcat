package com.project.fatcat.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findAllByOrderByMainCodeAscSubCodeAsc();
	
	// 디테일까지 선택
	Optional<Category> findByMainCodeAndSubCodeAndDetailCategory(String mainCode,
            String subCode,
            String detailCategory);

    // 서브까지만 선택
    List<Category> findByMainCodeAndSubCode(String mainCode, String subCode);

    // 메인만 선택
    List<Category> findByMainCode(String mainCode);
}
