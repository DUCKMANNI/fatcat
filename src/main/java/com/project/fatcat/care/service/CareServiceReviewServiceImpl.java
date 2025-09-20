package com.project.fatcat.care.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.fatcat.care.dto.CareReviewResopnseDto;
import com.project.fatcat.care.repository.CareServiceReviewRepository;
import com.project.fatcat.entity.CareServiceReview;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CareServiceReviewServiceImpl implements CareServiceReviewService {

    private final CareServiceReviewRepository careServiceReviewRepository;
    private final UserRepository userRepository;

 

    @Override
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
        return convertToDto(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CareReviewResopnseDto> getReviewsByTargetUserSeq(Integer targetUserSeq) {
        List<CareServiceReview> reviews = careServiceReviewRepository.findByTargetUserUserSeq(targetUserSeq);
        return reviews.stream()
                      .map(this::convertToDto)
                      .collect(Collectors.toList());
    }

    private CareReviewResopnseDto convertToDto(CareServiceReview entity) {
        CareReviewResopnseDto dto = new CareReviewResopnseDto();
        dto.setReviewSeq(entity.getCareReviewSeq());
        dto.setAuthorNickname(entity.getAuthorUser().getNickname());
        dto.setCareReview(entity.getCareReview());
        dto.setCareRating(entity.getCareRating());
        dto.setCreateDate(entity.getCreateDate());
        return dto;
    }
}