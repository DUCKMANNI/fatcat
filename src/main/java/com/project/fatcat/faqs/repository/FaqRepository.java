package com.project.fatcat.faqs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
	
	
	

    // 2. 검색을 위한 메서드 (기존)
    Page<Faq> findByQuestionContainingOrAnswerContaining(String questionKeyword, String answerKeyword, Pageable pageable);
    

}
