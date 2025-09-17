package com.project.fatcat.faqs.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.project.fatcat.entity.Faq;
import com.project.fatcat.faqs.repository.FaqRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FaqService {
	
	private final FaqRepository faqRepository;
	
	
//
//	    // 모든 FAQ 목록 조회
//	    public Page<Faq> getAllFaqs(Pageable pageable) {
//	        return faqRepository.findAll(pageable);
//	    }
//
//	    // 질문 등록 (누구나 가능)
//	    public void createQuestion(String question) {
//	        Faq faq = Faq.builder()
//	                .faqCode("USER_QNA")
//	                .question(question)
//	                .answer("답변 대기 중") // answer 필드가 NOT NULL이므로 기본값 설정
//	                .viewCount(0)
//	                .createDate(LocalDateTime.now())
//	                .build();
//	        
//	        faqRepository.save(faq);
//	    }
//
//	    // FAQ 상세 정보 조회
//	    public Optional<Faq> getFaqById(Integer faqSeq) {
//	        return faqRepository.findById(faqSeq);
//	    }
//
//	    // 답변 등록 (관리자만 가능)
//	    public void saveAnswer(Integer faqSeq, String answer) {
//	        Optional<Faq> optionalFaq = faqRepository.findById(faqSeq);
//	        if (optionalFaq.isPresent()) {
//	            Faq faq = optionalFaq.get();
//	            faq.setAnswer(answer); // answer 필드만 업데이트
//	            faqRepository.save(faq);
//	        }
//	    }
	
	
	 // 모든 FAQ 목록을 페이징 및 검색하여 조회
    public Page<Faq> getAllFaqs(Pageable pageable, String keyword) {
        if (StringUtils.hasText(keyword)) {
            return faqRepository.findByQuestionContainingOrAnswerContaining(keyword, keyword, pageable);
        }
        return faqRepository.findAll(pageable);
    }

    // 질문 등록 (누구나 가능)
    public void createQuestion(String question) {
        Faq faq = Faq.builder()
                .faqCode("USER_QNA")
                .question(question)
                .answer("답변 대기 중")
                .viewCount(0)
                .createDate(LocalDateTime.now())
                .build();
        
        faqRepository.save(faq);
    }

    // FAQ 상세 정보 조회
    public Optional<Faq> getFaqById(Integer faqSeq) {
        return faqRepository.findById(faqSeq);
    }

    // 답변 등록 (관리자만 가능)
    public void saveAnswer(Integer faqSeq, String answer) {
        Optional<Faq> optionalFaq = faqRepository.findById(faqSeq);
        if (optionalFaq.isPresent()) {
            Faq faq = optionalFaq.get();
            faq.setAnswer(answer);
            faqRepository.save(faq);
        }
    }

}
