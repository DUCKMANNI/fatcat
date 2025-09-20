package com.project.fatcat.vet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.VetRatingAvg;

public interface VetRatingAvgRepository extends JpaRepository<VetRatingAvg, Integer>{

	Optional<VetRatingAvg> findByVetClinicVetSeq(Integer vetSeq);
}
