package com.project.fatcat.vet.service;

import com.project.fatcat.entity.VetClinic;
import com.project.fatcat.entity.VetRatingAvg;
import com.project.fatcat.entity.VetReview;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.repository.UserRepository;
import com.project.fatcat.vet.dto.VetReviewDto;
import com.project.fatcat.vet.dto.VetReviewResponseDto;
import com.project.fatcat.vet.repository.VetClinicRepository;
import com.project.fatcat.vet.repository.VetRatingAvgRepository;
import com.project.fatcat.vet.repository.VetReviewRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VetServiceImpl implements VetService {

    private final VetClinicRepository vetClinicRepository;
    private final VetReviewRepository vetReviewRepository;
    private final VetRatingAvgRepository vetRatingAvgRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addReview(VetReviewDto reviewDto, Integer userSeq) { 
        // 1. 로그인 유효성 검사 (Controller에서 userSeq를 null로 넘겼을 경우 방어)
        if (userSeq == null || userSeq <= 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        
        // 2. 유저 엔티티 조회 (작성자 정보)
        User author = userRepository.findById(userSeq)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 사용자 ID입니다. 다시 로그인해주세요."));
        
        // 3. 병원 정보 처리
        VetClinic vetClinic = vetClinicRepository.findByClinicNameAndClinicAddress(reviewDto.getPlaceName(), reviewDto.getAddress())
            .orElseGet(() -> {
                VetClinic newClinic = VetClinic.builder()
                        .clinicName(reviewDto.getPlaceName())
                        .clinicAddress(reviewDto.getAddress()) 
                        .clinicCity("미지정")
                        .clinicTown("미지정")
                        .latitude(0.0)
                        .longitude(0.0)
                        .createDate(LocalDateTime.now())
                        .build();
                return vetClinicRepository.save(newClinic);
            });
        
        // 4. 리뷰 엔티티 생성 및 저장
        VetReview vetReview = VetReview.builder()
                .vetClinic(vetClinic)
                .vetReview(reviewDto.getReviewContent())
                .vetRating(reviewDto.getRating())
                .user(author) 
                .visitDate(LocalDateTime.now())
                .createDate(LocalDateTime.now())
                .build();
        vetReviewRepository.save(vetReview);

        updateRating(vetClinic, reviewDto.getRating());
    }

    @Override
    @Transactional(readOnly = true)
    public VetReviewResponseDto getClinicDetails(String placeName, String address) {
        VetClinic vetClinic = vetClinicRepository.findByClinicNameAndClinicAddress(placeName, address).orElse(null);
        VetReviewResponseDto responseDto = new VetReviewResponseDto();
        if (vetClinic != null) {
            VetRatingAvg ratingAvg = vetRatingAvgRepository.findByVetClinicVetSeq(vetClinic.getVetSeq()).orElse(null);
            List<VetReview> reviews = vetReviewRepository.findByVetClinicVetSeq(vetClinic.getVetSeq());
            if (ratingAvg != null) {
                responseDto.setRatingAvg(ratingAvg.getRatingAvg());
                responseDto.setRatingCount(ratingAvg.getRatingCount());
            }
            
            // ✅ NullPointerException 방지 로직
            List<VetReviewResponseDto.ReviewDetail> reviewDetails = reviews.stream()
                .map(review -> {
                    User user = review.getUser(); 
                    
                    Integer userSeq = (user != null) ? user.getUserSeq() : null;
                    String userName = (user != null) ? user.getUserName() : "탈퇴한 사용자"; 
                    
                    return new VetReviewResponseDto.ReviewDetail(
                        review.getVetReviewSeq(), 
                        review.getVetReview(), 
                        review.getVetRating(), 
                        review.getCreateDate(), 
                        review.getVisitDate(),
                        userSeq, 
                        userName 
                    );
                })
              	.collect(Collectors.toList());
            
            responseDto.setReviews(reviewDetails);
        }
        return responseDto;
    }

    @Override
    @Transactional
    public void updateReview(Integer vetReviewSeq, VetReviewDto reviewDto, Integer userSeq) { 
        VetReview vetReview = vetReviewRepository.findById(vetReviewSeq)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."));
        
        // 1. 로그인 유효성 검사
        if (userSeq == null || userSeq <= 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        
        // 2. DB 작성자 정보 Null 체크 및 권한 검증
        User author = vetReview.getUser();
        if (author == null) {
             throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자 정보가 없어 수정 권한을 확인할 수 없습니다.");
        }
        
        // 3. 작성자 UserSeq 검증
        if (!author.getUserSeq().equals(userSeq)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "리뷰 수정 권한이 없습니다.");
        }
        
        // 4. 리뷰 수정
        vetReview.setVetReview(reviewDto.getReviewContent());
        vetReview.setVetRating(reviewDto.getRating());
        vetReview.setUpdateDate(LocalDateTime.now());
        
        recalculateRating(vetReview.getVetClinic());
    }
    
    @Override
    @Transactional
    public void deleteReview(Integer vetReviewSeq, Integer userSeq) { 
        VetReview vetReview = vetReviewRepository.findById(vetReviewSeq)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."));
            
        // 1. 로그인 유효성 검사
        if (userSeq == null || userSeq <= 0) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 2. DB 작성자 정보 Null 체크 및 권한 검증
        User author = vetReview.getUser();
        if (author == null) {
             throw new ResponseStatusException(HttpStatus.FORBIDDEN, "작성자 정보가 없어 삭제 권한을 확인할 수 없습니다.");
        }
        
        // 3. 작성자 UserSeq 검증
        if (!author.getUserSeq().equals(userSeq)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "리뷰 삭제 권한이 없습니다.");
        }
            
        // 4. 리뷰 삭제
        VetClinic vetClinic = vetReview.getVetClinic();
        vetReviewRepository.delete(vetReview);
        
        recalculateRating(vetClinic);
    }
    
    private void recalculateRating(VetClinic vetClinic) {
        List<VetReview> reviews = vetReviewRepository.findByVetClinicVetSeq(vetClinic.getVetSeq());
        VetRatingAvg ratingAvg = vetRatingAvgRepository.findByVetClinicVetSeq(vetClinic.getVetSeq())
            .orElseGet(() -> VetRatingAvg.builder().vetClinic(vetClinic).build());
        
        if (reviews.isEmpty()) {
            ratingAvg.setRatingSum(0);
            ratingAvg.setRatingCount(0);
            ratingAvg.setRatingAvg(BigDecimal.ZERO);
        } else {
            int sum = reviews.stream().mapToInt(VetReview::getVetRating).sum();
            int count = reviews.size();
            ratingAvg.setRatingSum(sum);
            ratingAvg.setRatingCount(count);
            ratingAvg.setRatingAvg(new BigDecimal(sum).divide(new BigDecimal(count), 2, RoundingMode.HALF_UP));
        }
        ratingAvg.setLastReviewDate(LocalDateTime.now());
        vetRatingAvgRepository.save(ratingAvg);
    }
    
    private void updateRating(VetClinic vetClinic, Integer newRating) {
        VetRatingAvg ratingAvg = vetRatingAvgRepository.findByVetClinicVetSeq(vetClinic.getVetSeq())
            .orElse(VetRatingAvg.builder()
                .vetClinic(vetClinic)
                .ratingCount(0)
                .ratingSum(0)
                .ratingAvg(BigDecimal.ZERO)
                .build());
        
        ratingAvg.setRatingCount(ratingAvg.getRatingCount() + 1);
        ratingAvg.setRatingSum(ratingAvg.getRatingSum() + newRating);
        
        if (ratingAvg.getRatingCount() > 0) {
            ratingAvg.setRatingAvg(new BigDecimal(ratingAvg.getRatingSum()).divide(new BigDecimal(ratingAvg.getRatingCount()),
            		2, RoundingMode.HALF_UP));
        } else {
            ratingAvg.setRatingAvg(BigDecimal.ZERO);
        }
        ratingAvg.setLastReviewDate(LocalDateTime.now());

        vetRatingAvgRepository.save(ratingAvg);
    }
}