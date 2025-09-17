package com.project.fatcat.care.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.CareServiceBoard;

public interface CareServiceBoardRepository extends JpaRepository<CareServiceBoard, Integer>{

	List<CareServiceBoard> findByAddress1AndAddress2(String address1, String address2);
	
	List<CareServiceBoard> findByAddress1(String address1);
}
