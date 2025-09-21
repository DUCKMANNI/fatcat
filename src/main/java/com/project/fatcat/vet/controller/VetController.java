package com.project.fatcat.vet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.fatcat.vet.dto.VetReviewDto;
import com.project.fatcat.vet.dto.VetReviewResponseDto;
import com.project.fatcat.vet.service.VetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vet-clinics/reviews")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    // GET: 특정 병원의 리뷰 및 평점 정보 조회
    @GetMapping
    public ResponseEntity<VetReviewResponseDto> getClinicReviews(
            @RequestParam("placeName") String placeName, @RequestParam("address") String address) {
        VetReviewResponseDto reviews = vetService.getClinicDetails(placeName, address);
        return ResponseEntity.ok(reviews);
    }

    // POST: 리뷰 등록
    @PostMapping
    public ResponseEntity<Void> addReview(@RequestBody VetReviewDto reviewDto) {
        vetService.addReview(reviewDto);
        return ResponseEntity.ok().build();
    }
    
    // PUT: 리뷰 수정
    @PutMapping("/{vetReviewSeq}")
    public ResponseEntity<Void> updateReview(@PathVariable("vetReviewSeq") Integer vetReviewSeq, @RequestBody VetReviewDto reviewDto) {
        vetService.updateReview(vetReviewSeq, reviewDto);
        return ResponseEntity.ok().build();
    }
    
    // DELETE: 리뷰 삭제
    @DeleteMapping("/{vetReviewSeq}")
    public ResponseEntity<Void> deleteReview(@PathVariable("vetReviewSeq") Integer vetReviewSeq) {
        vetService.deleteReview(vetReviewSeq);
        return ResponseEntity.ok().build();
    }
}