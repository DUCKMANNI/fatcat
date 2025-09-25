package com.project.fatcat.care.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller; // @RestController -> @Controller로 변경
import org.springframework.ui.Model; // Model 객체 사용을 위해 추가
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody; // API 응답을 위해 @ResponseBody 추가

import com.project.fatcat.care.dto.CareReviewResopnseDto;
import com.project.fatcat.care.dto.CareReviewRequestDto;
import com.project.fatcat.care.service.CareServiceReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/care-reviews")
@RequiredArgsConstructor
public class CareServiceReviewController {

    private final CareServiceReviewService careServiceReviewService;

    @GetMapping
    @ResponseBody // JSON 데이터를 반환하기 위해 필요
    public ResponseEntity<List<CareReviewResopnseDto>> getReviews(@RequestParam("targetUserSeq") Integer targetUserSeq) {
        List<CareReviewResopnseDto> reviews = careServiceReviewService.getReviewsByTargetUserSeq(targetUserSeq);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping
    @ResponseBody // JSON 데이터를 반환하기 위해 필요
    public ResponseEntity<CareReviewResopnseDto> createReview(@RequestBody CareReviewRequestDto request) {
    	
       
        
        CareReviewResopnseDto newReview = careServiceReviewService.createReview(
            request.getAuthorUserSeq(),
            request.getTargetUserSeq(),
            request.getCareReview(),
            request.getCareRating(),
            request.getTargetRole()
        );
        return ResponseEntity.ok(newReview);
    }
}