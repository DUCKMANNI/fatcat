package com.project.fatcat.shopping.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Category;
import com.project.fatcat.entity.Product;
import com.project.fatcat.shopping.repository.CategoryRepository;
import com.project.fatcat.shopping.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	@Override
    public Page<Product> getProducts(String main, String sub, String detail, String keyword, Pageable pageable) {

        // ✅ 1. 검색어가 들어온 경우 → 카테고리 무시하고 전체에서 검색
        if (keyword != null && !keyword.isBlank()) {
            return productRepository.findByProductNameContaining(keyword, pageable);
        }

        // ✅ 2. 카테고리 기반 조회
        if (detail != null) {
            // 디테일 카테고리 선택
            Category category = categoryRepository
                    .findByMainCodeAndSubCodeAndDetailCategory(main, sub, detail)
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));

            return productRepository.findByCategory_CategorySeq(category.getCategorySeq(), pageable);

        } else if (sub != null) {
            // 서브 카테고리 전체
            return productRepository.findBySubCategory(main, sub, pageable);

        } else if (!"all".equalsIgnoreCase(main)) {
            // 메인 카테고리 전체
            return productRepository.findByMainCategory(main, pageable);
        } else {
            // ✅ 전체 상품 (카테고리 미선택 & 검색어 없음)
            return productRepository.findAll(pageable);
        }
    }

    public Product getProductDetail(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + productCode));
    }
	
}
