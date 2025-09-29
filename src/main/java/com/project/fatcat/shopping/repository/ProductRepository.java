package com.project.fatcat.shopping.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	// FK 바로 조회
	@EntityGraph(attributePaths = "productImageList")
    Page<Product> findByCategory_CategorySeq(Integer categorySeq, Pageable pageable);

    // 메인 카테고리 전체 조회
	@EntityGraph(attributePaths = "productImageList")
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.mainCode = :mainCode")
    Page<Product> findByMainCategory(@Param("mainCode") String mainCode, Pageable pageable);

    // 서브 카테고리 전체 조회
	@EntityGraph(attributePaths = "productImageList")
    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.mainCode = :mainCode AND c.subCode = :subCode")
    Page<Product> findBySubCategory(@Param("mainCode") String mainCode,
                                    @Param("subCode") String subCode,
                                    Pageable pageable);
	
	// 상세 조회
    @EntityGraph(attributePaths = "productImageList")
    Optional<Product> findByProductCode(String productCode);
    
    Page<Product> findByProductNameContaining(String keyword, Pageable pageable);


	
}
