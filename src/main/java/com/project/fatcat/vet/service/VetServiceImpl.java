package com.project.fatcat.vet.service;

import com.project.fatcat.entity.VetClinic;
import com.project.fatcat.entity.VetRatingAvg;
import com.project.fatcat.entity.VetReview;
import com.project.fatcat.vet.dto.VetReviewDto;
import com.project.fatcat.vet.dto.VetReviewResponseDto;
import com.project.fatcat.vet.repository.VetClinicRepository;
import com.project.fatcat.vet.repository.VetRatingAvgRepository;
import com.project.fatcat.vet.repository.VetReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Override
    @Transactional
    public void addReview(VetReviewDto reviewDto) {
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
        
        VetReview vetReview = VetReview.builder()
                .vetClinic(vetClinic)
                .vetReview(reviewDto.getReviewContent())
                .vetRating(reviewDto.getRating())
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
            List<VetReviewResponseDto.ReviewDetail> reviewDetails = reviews.stream()
                .map(review -> new VetReviewResponseDto.ReviewDetail(review.getVetReviewSeq(), review.getVetReview(), review.getVetRating(), review.getCreateDate(), review.getVisitDate()))
              	.collect(Collectors.toList());
            responseDto.setReviews(reviewDetails);
        }
        return responseDto;
    }

    @Override
    @Transactional
    public void updateReview(Integer vetReviewSeq, VetReviewDto reviewDto) {
        VetReview vetReview = vetReviewRepository.findById(vetReviewSeq)
            .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + vetReviewSeq));
        
        vetReview.setVetReview(reviewDto.getReviewContent());
        vetReview.setVetRating(reviewDto.getRating());
        vetReview.setUpdateDate(LocalDateTime.now());
        vetReviewRepository.save(vetReview);
        
        recalculateRating(vetReview.getVetClinic());
    }
    
    @Override
    @Transactional
    public void deleteReview(Integer vetReviewSeq) {
        VetReview vetReview = vetReviewRepository.findById(vetReviewSeq)
            .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + vetReviewSeq));
            
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