package com.project.fatcat.catBoard.repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.KnowledgePost;


public interface PostRepository extends JpaRepository<KnowledgePost, Integer> {

	 // 게시판 코드로 게시글 페이징 조회 (JPQL)
//    @Query("SELECT p FROM KnowledgePost p WHERE p.knowledgeBoard.boardCode = :boardCode")
//    Page<KnowledgePost> findPostsByBoardCode(@Param("boardCode") String boardCode, Pageable pageable);
//    
//    @Query("SELECT p FROM KnowledgePost p JOIN FETCH p.knowledgeBoard WHERE p.postSeq = :seq")
//    Optional<KnowledgePost> findByIdWithBoard(@Param("seq") Integer seq);
    
	Page<KnowledgePost> findAll(Pageable pageable);
	Page<KnowledgePost> findByKnowledgeBoard_BoardSeq(Integer boardSeq, Pageable pageable);

    
	 
}
	

