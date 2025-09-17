package com.project.fatcat.care.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.fatcat.entity.CareSession;

@Repository
public interface CareSessionRepository extends JpaRepository<CareSession, Integer>{
	
	@Query("SELECT s FROM CareSession s JOIN FETCH s.sitterUser JOIN FETCH s.ownerUser")
	List<CareSession> findAllwithUsers();
	
	@Query("SELECT s FROM CareSession s JOIN FETCH s.sitterUser JOIN FETCH s.ownerUser "
			+ "WHERE s.ownerUser.userSeq = :userSeq OR s.sitterUser.userSeq = :userSeq")
	List<CareSession> findByOwnerUserOrSitterUser(@Param("userSeq") Integer userSeq);

}
