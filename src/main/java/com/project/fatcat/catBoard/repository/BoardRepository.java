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
	
	Optional<KnowledgeBoard> findByBoardCode(String boardCode);
	 
}
