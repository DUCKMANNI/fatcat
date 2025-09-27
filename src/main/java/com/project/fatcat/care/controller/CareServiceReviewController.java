package com.project.fatcat.care.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping; // ⭐ DELETE 임포트 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // ⭐ PathVariable 임포트 추가
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // ⭐ PUT 임포트 추가
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.fatcat.care.dto.CareReviewRequestDto;
import com.project.fatcat.care.dto.CareReviewResopnseDto;
import com.project.fatcat.care.service.CareServiceReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/care-reviews")
@RequiredArgsConstructor
public class CareServiceReviewController {

    private final CareServiceReviewService careServiceReviewService;

    // 1. 리뷰 조회 GET /api/care-reviews?targetUserSeq={id}
    @GetMapping
    @ResponseBody 
    public ResponseEntity<List<CareReviewResopnseDto>> getReviews(@RequestParam("targetUserSeq") Integer targetUserSeq) {
        List<CareReviewResopnseDto> reviews = careServiceReviewService.getReviewsByTargetUserSeq(targetUserSeq);
        return ResponseEntity.ok(reviews);
    }

    // 2. 리뷰 생성 POST /api/care-reviews
    @PostMapping
    @ResponseBody 
    public ResponseEntity<CareReviewResopnseDto> createReview(@RequestBody CareReviewRequestDto request) {
    	
        CareReviewResopnseDto newReview = careServiceReviewService.createReview(
            request.getAuthorUserSeq(),
            request.getTargetUserSeq(),
            request.getCareReview(),
            request.getCareRating(),
            request.getTargetRole()
        );
        // 생성 성공 시 201 Created 반환
        return new ResponseEntity<>(newReview, HttpStatus.CREATED);
    }
    
    // 3. ⭐ 리뷰 수정 PUT /api/care-reviews/{reviewId}
    @PutMapping("/{reviewId}")
    @ResponseBody
    public ResponseEntity<CareReviewResopnseDto> updateReview(
        @PathVariable("reviewId") Integer reviewId,
        @RequestBody CareReviewRequestDto request) {
        
        // Service Layer에서 리뷰 ID와 수정 요청 데이터를 처리
        CareReviewResopnseDto updatedReview = careServiceReviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(updatedReview);
    }
    
    // 4. ⭐ 리뷰 삭제 DELETE /api/care-reviews/{reviewId}?authorUserSeq={id}
    @DeleteMapping("/{reviewId}")
    @ResponseBody
    public ResponseEntity<Void> deleteReview(
        @PathVariable("reviewId") Integer reviewId,
        // 현재는 인증/권한이 없으므로, 작성자 ID를 쿼리 파라미터로 받아 Service로 전달
        @RequestParam("authorUserSeq") Integer authorUserSeq) {
        
        // Service Layer에서 삭제 로직 및 작성자 일치 검증을 수행
        careServiceReviewService.deleteReview(reviewId, authorUserSeq);
        // 삭제 성공 시 204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}