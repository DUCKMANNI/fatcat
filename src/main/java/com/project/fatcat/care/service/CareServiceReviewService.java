package com.project.fatcat.care.service;

import java.util.List;

import com.project.fatcat.care.dto.CareReviewResopnseDto;


public interface CareServiceReviewService {
    CareReviewResopnseDto createReview(Integer authorUserSeq, Integer targetUserSeq, String careReview, Integer careRating, String targetRole);
    List<CareReviewResopnseDto> getReviewsByTargetUserSeq(Integer targetUserSeq);
}