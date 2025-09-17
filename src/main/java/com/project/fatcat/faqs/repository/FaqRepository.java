package com.project.fatcat.faqs.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Faq;

public interface FaqRepository extends JpaRepository<Faq, Integer> {
	
	Page<Faq> findByQuestionContainingOrAnswerContaining(String questionKeyword, String answerKeyword, Pageable pageable);

}
