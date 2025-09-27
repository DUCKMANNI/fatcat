package com.project.fatcat.care.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.fatcat.care.dto.CareReviewResopnseDto;
import com.project.fatcat.care.dto.CareReviewRequestDto; // DTO 임포트 추가
import com.project.fatcat.care.repository.CareServiceReviewRepository;
import com.project.fatcat.entity.CareServiceReview;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareServiceReviewServiceImpl implements CareServiceReviewService {

    private final CareServiceReviewRepository careServiceReviewRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CareReviewResopnseDto createReview(Integer authorUserSeq, Integer targetUserSeq, String careReview, Integer careRating, String targetRole) {
    	
        User authorUser = userRepository.findById(authorUserSeq).orElseThrow(() -> new RuntimeException("작성자를 찾을 수 없습니다."));
        User targetUser = userRepository.findById(targetUserSeq).orElseThrow(() -> new RuntimeException("대상 사용자를 찾을 수 없습니다."));

        CareServiceReview review = new CareServiceReview();
        review.setAuthorUser(authorUser);
        review.setTargetUser(targetUser);
        review.setCareReview(careReview);
        review.setCareRating(careRating);
        review.setTargetRole(targetRole);
        review.setCreateDate(LocalDateTime.now());
        
        CareServiceReview savedReview = careServiceReviewRepository.save(review);
        return convertToDtoWithAuthorSeq(savedReview); // ⭐ 변경된 DTO 변환 메서드 사용
    }

    @Override
    @Transactional(readOnly = true)
    public List<CareReviewResopnseDto> getReviewsByTargetUserSeq(Integer targetUserSeq) {
        List<CareServiceReview> reviews = careServiceReviewRepository.findByTargetUserUserSeq(targetUserSeq);
        return reviews.stream()
                      .map(this::convertToDtoWithAuthorSeq)
                      .collect(Collectors.toList());
    }

    // ⭐ 3. 리뷰 수정 구현: 작성자 일치 여부만 검증
    @Override
    @Transactional
    public CareReviewResopnseDto updateReview(Integer reviewId, CareReviewRequestDto updateRequest) {
        
        CareServiceReview review = careServiceReviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // ⭐ 작성자 검증: 요청 DTO의 authorUserSeq와 리뷰 작성자가 일치하는지 확인
        if (!review.getAuthorUser().getUserSeq().equals(updateRequest.getAuthorUserSeq())) {
            throw new RuntimeException("리뷰를 수정할 권한이 없습니다.");
        }

        // 데이터 업데이트
        review.setCareReview(updateRequest.getCareReview());
        review.setCareRating(updateRequest.getCareRating());

        CareServiceReview updatedReview = careServiceReviewRepository.save(review);
        return convertToDtoWithAuthorSeq(updatedReview);
    }

    // ⭐ 4. 리뷰 삭제 구현: 작성자 일치 여부만 검증
    @Override
    @Transactional
    public void deleteReview(Integer reviewId, Integer authorUserSeq) {
        
        CareServiceReview review = careServiceReviewRepository.findById(reviewId)
            .orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));

        // ⭐ 작성자 검증: 요청 Query Param의 authorUserSeq와 리뷰 작성자가 일치하는지 확인
        if (!review.getAuthorUser().getUserSeq().equals(authorUserSeq)) {
            throw new RuntimeException("리뷰를 삭제할 권한이 없습니다.");
        }

        careServiceReviewRepository.delete(review);
    }
    
    // ⭐ 5. DTO 변환 메서드 (작성자 ID 포함)
    private CareReviewResopnseDto convertToDtoWithAuthorSeq(CareServiceReview entity) {
        CareReviewResopnseDto dto = new CareReviewResopnseDto();
        dto.setReviewSeq(entity.getCareReviewSeq());
        dto.setAuthorUserSeq(entity.getAuthorUser().getUserSeq()); // ⭐ 작성자 ID 포함
        dto.setAuthorNickname(entity.getAuthorUser().getNickname());
        dto.setCareReview(entity.getCareReview());
        dto.setCareRating(entity.getCareRating());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }
    // 기존 convertToDto 메서드는 삭제하거나 위 메서드로 통일
}