package com.project.fatcat.care.service;

import java.util.List;

import com.project.fatcat.care.dto.CareReviewResopnseDto;
import com.project.fatcat.care.dto.CareReviewRequestDto;

public interface CareServiceReviewService {

    CareReviewResopnseDto createReview(Integer authorUserSeq, Integer targetUserSeq, String careReview, Integer careRating, String targetRole);

    List<CareReviewResopnseDto> getReviewsByTargetUserSeq(Integer targetUserSeq);

	
	CareReviewResopnseDto updateReview(Integer reviewId, CareReviewRequestDto updateRequest);

	void deleteReview(Integer reviewId, Integer authorUserSeq); 
}