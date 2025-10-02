

package com.project.fatcat.inquiry.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.project.fatcat.SecurityUtils;
import com.project.fatcat.entity.Inquiry;
import com.project.fatcat.entity.InquiryComment;
import com.project.fatcat.entity.User;
import com.project.fatcat.inquiry.repository.InquiryCommentRepository;
import com.project.fatcat.inquiry.repository.InquiryRepository;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional 
public class InquiryCommentService {
	private final InquiryCommentRepository inquiryCommentRepository;
	private final InquiryRepository inquiryRepository;
	private final UserRepository userRepository;

    // 답변 등록 (관리자만 가능)
    public void saveAnswer(Integer inquirySeq, String inquiryComment) {
        
        Inquiry inquiry = inquiryRepository.findById(inquirySeq)
            .orElseThrow(() -> new IllegalArgumentException("해당 문의(InquirySeq: " + inquirySeq + ")를 찾을 수 없습니다."));
        
        try {
            // ⭐ SecurityUtil 사용
            Integer adminSeq = SecurityUtils.getCurrentUserSeq(); 
            
            // 관리자 ID null 체크 (인증 문제 방어)
            if (adminSeq == null) {
                throw new IllegalStateException("답변을 등록할 수 있는 현재 로그인된 관리자 정보를 찾을 수 없습니다."); 
            }
            
            User admin = userRepository.findById(adminSeq)
                .orElseThrow(() -> new IllegalStateException("DB에서 답변자(Admin) UserSeq: " + adminSeq + "를 찾을 수 없습니다."));
            
            // 답변 내용이 비어있는지 확인
            Assert.hasText(inquiryComment, "답변 내용은 비어 있을 수 없습니다.");
            
            InquiryComment comment = InquiryComment.builder()
                    .inquiry(inquiry) 
                    .user(admin)       
                    .inquiryComment(inquiryComment) 
                    .createDate(LocalDateTime.now())
                    .isDeleted(false)
                    .build();
            
            inquiryCommentRepository.save(comment);

            // ⭐⭐⭐ [수정] 영속성 컨텍스트 오류 방지를 위해 이 코드 블록을 제거했습니다. ⭐⭐⭐
            /*
            if (inquiry.getInquiryCommentList() != null) {
                inquiry.getInquiryCommentList().add(comment);
            }
            */
            // ⭐⭐⭐ (수정 완료) ⭐⭐⭐
            
        } catch (IllegalStateException e) {
             // 관리자 로그인 정보 관련 예외
            throw new RuntimeException("답변 등록 실패: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("문의 답변 저장 중 오류가 발생했습니다.", e);
        }
    }
}