package com.project.fatcat.vet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.VetReview;

public interface VetReviewRepository extends JpaRepository<VetReview, Integer>{

	List<VetReview> findByVetClinicVetSeq(Integer vetSeq);
}
