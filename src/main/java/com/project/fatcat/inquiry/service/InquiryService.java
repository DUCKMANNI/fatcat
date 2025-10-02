//package com.project.fatcat.inquiry.service;
//
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import com.project.fatcat.SecurityUtils;
//import com.project.fatcat.entity.Inquiry;
//import com.project.fatcat.entity.User; // User 엔티티가 필요하다고 가정합니다.
//import com.project.fatcat.inquiry.form.InquiryForm;
//import com.project.fatcat.inquiry.repository.InquiryCommentRepository;
//import com.project.fatcat.inquiry.repository.InquiryRepository;
//import com.project.fatcat.users.repository.UserRepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class InquiryService {
//	
//	private final InquiryRepository inquiryRepository;
//    private final InquiryCommentRepository inquiryCommentRepository;
//    private final UserRepository userRepository;
//
//    
//	 // 모든 문의 목록을 페이징 및 검색하여 조회
//    @Transactional(readOnly = true)
//    public Page<Inquiry> getAllInquiries(Pageable pageable, String keyword) {
//        if (StringUtils.hasText(keyword)) {
//            // 이 Repository 메서드는 findByInquiryTitleContainingOrInquiryContentContainingAndIsDeletedFalse로 수정되어야 합니다.
//            return inquiryRepository.findByInquiryTitleContainingOrInquiryContentContaining(keyword, keyword, pageable);
//        }
//        return inquiryRepository.findByIsDeletedFalse(pageable);
//    }
//
//    // 질문 등록 (누구나 가능)
//    public void createQuestion(InquiryForm inquiryTitle, InquiryForm inquiryContent) {
//    	try {
//            // ⭐ 현재 로그인된 사용자 정보 획득
//    	    Integer userSeq = SecurityUtils.getCurrentUserSeq();
//            User user = userRepository.findById(userSeq).orElse(null); 
//
//            Inquiry inquiry = Inquiry.builder()
//                    .user(user) 
//                    .inquiryTitle(inquiryTitle)
//                    .inquiryContent(inquiryContent)
//                    // 엔티티의 @ColumnDefinition에 맞춰 기본값 명시
//                    .viewCount(0) 
//                    .createDate(LocalDateTime.now())
//                    .isDeleted(false)
//                    .build();
//            
//            inquiryRepository.save(inquiry);
//        } catch (IllegalStateException e) {
//            // SecurityUtils.getCurrentUserSeq()에서 발생할 수 있는 로그인 필요 예외 처리
//            throw new RuntimeException("질문 등록 실패: 로그인 정보가 유효하지 않습니다.", e);
//        }
//    }
//    // 문의 상세 정보 조회 및 조회수 증가
//    @Transactional
//    public Optional<Inquiry> getInquiryById(Integer inquirySeq) {
//        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(inquirySeq);
//        if (optionalInquiry.isPresent()) {
//            Inquiry inquiry = optionalInquiry.get();
//            inquiry.setViewCount(inquiry.getViewCount() + 1);
//        }
//        return optionalInquiry;
//    }
//}


package com.project.fatcat.inquiry.service;


import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.project.fatcat.SecurityUtils;
import com.project.fatcat.entity.Inquiry;
import com.project.fatcat.entity.User; // User 엔티티가 필요하다고 가정합니다.
import com.project.fatcat.inquiry.form.InquiryForm;
import com.project.fatcat.inquiry.repository.InquiryCommentRepository;
import com.project.fatcat.inquiry.repository.InquiryRepository;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {
	
	private final InquiryRepository inquiryRepository;
    private final InquiryCommentRepository inquiryCommentRepository;
    private final UserRepository userRepository;

    
	 // 모든 문의 목록을 페이징 및 검색하여 조회
    @Transactional(readOnly = true)
    public Page<Inquiry> getAllInquiries(Pageable pageable, String keyword) {
        if (StringUtils.hasText(keyword)) {
            // 이 Repository 메서드는 findByInquiryTitleContainingOrInquiryContentContainingAndIsDeletedFalse로 수정되어야 합니다.
            return inquiryRepository.findByInquiryTitleContainingOrInquiryContentContaining(keyword, keyword, pageable);
        }
        return inquiryRepository.findByIsDeletedFalse(pageable);
    }

    // ⭐ 수정된 부분: 질문 등록 (매개변수를 InquiryForm DTO 하나로 받고, 내부에서 Getter 사용)
    public void createQuestion(InquiryForm inquiryForm) {
    	try {
            // ⭐ 현재 로그인된 사용자 정보 획득
    	    Integer userSeq = SecurityUtils.getCurrentUserSeq();
            User user = userRepository.findById(userSeq).orElse(null); 

            Inquiry inquiry = Inquiry.builder()
                    .user(user) 
                    // ⭐ 수정: InquiryForm 객체에서 제목을 가져옴
                    .inquiryTitle(inquiryForm.getInquiryTitle())
                    // ⭐ 수정: InquiryForm 객체에서 내용을 가져옴
                    .inquiryContent(inquiryForm.getInquiryContent())
                    .viewCount(0) 
                    .createDate(LocalDateTime.now())
                    .isDeleted(false)
                    .build();
            
            inquiryRepository.save(inquiry);
        } catch (IllegalStateException e) {
            // SecurityUtils.getCurrentUserSeq()에서 발생할 수 있는 로그인 필요 예외 처리
            throw new RuntimeException("질문 등록 실패: 로그인 정보가 유효하지 않습니다.", e);
        }
    }
    
    // 문의 상세 정보 조회 및 조회수 증가
    @Transactional
    public Optional<Inquiry> getInquiryById(Integer inquirySeq) {
        Optional<Inquiry> optionalInquiry = inquiryRepository.findById(inquirySeq);
        if (optionalInquiry.isPresent()) {
            Inquiry inquiry = optionalInquiry.get();
            inquiry.setViewCount(inquiry.getViewCount() + 1);
        }
        return optionalInquiry;
    }
}