package com.project.fatcat.care.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.CareSession;

public interface CareSessionRepository extends JpaRepository<CareSession, Integer> {
	
	List<CareSession> findByOwnerUser_UserSeq(Integer userSeq);
	List<CareSession> findBySitterUser_UserSeq(Integer userSeq);

}
