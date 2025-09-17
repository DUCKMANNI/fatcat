package com.project.fatcat.catBoard.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;



public interface BoardRepository extends JpaRepository<KnowledgeBoard, Integer> {
	
//	 // KnowledgeBoard의 boardCode로 게시글 조회
//	@Query("SELECT p FROM KnowledgePost p WHERE p.knowledgeBoard.boardCode = :boardCode")
//	Page<KnowledgePost> findPostsByBoardCode(@Param("boardCode") String boardCode, Pageable pageable);
//	
//	 Optional<KnowledgeBoard> findByBoardCode(String boardCode);
	
	Optional<KnowledgeBoard> findByBoardCode(String boardCode);
	 
}
