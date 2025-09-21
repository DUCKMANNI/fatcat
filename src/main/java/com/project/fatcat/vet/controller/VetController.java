package com.project.fatcat.vet.controller;

import com.project.fatcat.vet.dto.VetReviewDto;
import com.project.fatcat.vet.dto.VetReviewResponseDto;
import com.project.fatcat.vet.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vet")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    // 지도 화면을 보여주는 GET 요청
    @GetMapping("/map")
    public String showVetMap() {
        return "vet_map"; // src/main/resources/templates/vet_map.html 렌더링
    }

    // ⭐ REST API 처리를 위한 별도 컨트롤러 분리 (내부 클래스로 간소화) ⭐
    @RestController
    @RequestMapping("/api/vet-clinics")
    @RequiredArgsConstructor
    public class VetApiController {

        private final VetService vetService;

        // GET: 특정 병원의 리뷰 및 평점 정보 조회
        @GetMapping("/reviews")
        public ResponseEntity<VetReviewResponseDto> getClinicReviews(@RequestParam String placeName, @RequestParam String address) {
            VetReviewResponseDto reviews = vetService.getClinicDetails(placeName, address);
            return ResponseEntity.ok(reviews);
        }

        // POST: 리뷰 등록
        @PostMapping("/reviews")
        public ResponseEntity<Void> addReview(@RequestBody VetReviewDto reviewDto) {
            vetService.addReview(reviewDto);
            return ResponseEntity.ok().build();
        }
    }
}