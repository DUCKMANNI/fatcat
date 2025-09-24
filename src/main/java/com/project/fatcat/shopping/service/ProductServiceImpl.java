package com.project.fatcat.shopping.service;

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
	public Page<Product> getProducts(String main, String sub, String detail, Pageable pageable){
		
		if (detail != null) {
            // 디테일 선택 시 FK로 조회
            Category category = categoryRepository
                    .findByMainCodeAndSubCodeAndDetailCategory(main, sub, detail)
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
            
            return productRepository.findByCategory_CategorySeq(category.getCategorySeq(), pageable);

        } else if (sub != null) {
            // 서브 전체
            return productRepository.findBySubCategory(main, sub, pageable);

        } else {
            // 메인 전체
            return productRepository.findByMainCategory(main, pageable);
        }
	}
	
	public Product getProductDetail(String productCode) {
		
		return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + productCode));
	}
}
