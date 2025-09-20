package com.project.fatcat.care.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fatcat.entity.CareServiceReview;

@Repository
public interface CareServiceReviewRepository extends JpaRepository<CareServiceReview, Integer>{

	//특정사용자가 받은 모든 리뷰를 찾는 메소드 
	List<CareServiceReview> findByTargetUserUserSeq(Integer targerUserSeq);
	
}
