package com.project.fatcat.vet.service;

import com.project.fatcat.vet.dto.VetReviewDto;
import com.project.fatcat.vet.dto.VetReviewResponseDto;

public interface VetService {

	void addReview(VetReviewDto reivewDto);

	VetReviewResponseDto getClinicDetails(String placeName, String address);

	void updateReview(Integer vetReviewSeq, VetReviewDto reviewDto);

	void deleteReview(Integer vetReviewSeq);

}
