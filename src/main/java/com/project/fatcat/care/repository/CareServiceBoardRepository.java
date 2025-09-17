package com.project.fatcat.care.repository;

import com.project.fatcat.entity.CareServiceBoard;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareServiceBoardRepository extends JpaRepository<CareServiceBoard, Integer> {

	List<CareServiceBoard> findByAddress1StartingWith(String region);
	
	
    
}