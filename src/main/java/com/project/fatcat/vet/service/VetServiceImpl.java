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
        VetClinic vetClinic = vetClinicRepository.findByClinicNameAndClinicAddress(placeName, address)
            .orElse(null);

        VetReviewResponseDto responseDto = new VetReviewResponseDto();
        
        if (vetClinic != null) {
            VetRatingAvg ratingAvg = vetRatingAvgRepository.findByVetClinicVetSeq(vetClinic.getVetSeq())
                .orElse(null);
            List<VetReview> reviews = vetReviewRepository.findByVetClinicVetSeq(vetClinic.getVetSeq());
            
            if (ratingAvg != null) {
                responseDto.setRatingAvg(ratingAvg.getRatingAvg());
                responseDto.setRatingCount(ratingAvg.getRatingCount());
            }
            
            List<VetReviewResponseDto.ReviewDetail> reviewDetails = reviews.stream()
                .map(review -> new VetReviewResponseDto.ReviewDetail(
                    review.getVetReview(), 
                    review.getVetRating(), 
                    review.getCreateDate(), null))
                .collect(Collectors.toList());
            
            responseDto.setReviews(reviewDetails);
        }
        
        return responseDto;
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
        ratingAvg.setRatingAvg(new BigDecimal(ratingAvg.getRatingSum()).divide(new BigDecimal(ratingAvg.getRatingCount()), 2, RoundingMode.HALF_UP));
        ratingAvg.setLastReviewDate(LocalDateTime.now());

        vetRatingAvgRepository.save(ratingAvg);
    }
}