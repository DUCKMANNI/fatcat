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


public interface PostRepository extends JpaRepository<KnowledgePost, Integer> {

	 // 게시판 코드로 게시글 페이징 조회 (JPQL)
//    @Query("SELECT p FROM KnowledgePost p WHERE p.knowledgeBoard.boardCode = :boardCode")
//    Page<KnowledgePost> findPostsByBoardCode(@Param("boardCode") String boardCode, Pageable pageable);
//    
//    @Query("SELECT p FROM KnowledgePost p JOIN FETCH p.knowledgeBoard WHERE p.postSeq = :seq")
//    Optional<KnowledgePost> findByIdWithBoard(@Param("seq") Integer seq);

	
	
	@Query("SELECT p FROM KnowledgePost p LEFT JOIN FETCH p.knowledgeCommentList WHERE p.postSeq = :postSeq")
	Optional<KnowledgePost> findByIdWithComments(@Param("postSeq") Integer postSeq);
	
	
	@Query("SELECT DISTINCT p FROM KnowledgePost p LEFT JOIN FETCH p.knowledgeCommentList WHERE p.knowledgeBoard.boardSeq = :boardSeq")
	List<KnowledgePost> findAllWithCommentsByBoard(@Param("boardSeq") Integer boardSeq);

	
	
	// KnowledgePostRepository
	@Query("SELECT p FROM KnowledgePost p LEFT JOIN FETCH p.knowledgeCommentList WHERE p.postSeq = :postSeq")
	List<KnowledgePost> findAllWithCommentsByBoardSeq(@Param("postSeq") Integer boardSeq);
	
	Page<KnowledgePost> findAll(Pageable pageable);
	Page<KnowledgePost> findByKnowledgeBoardBoardSeq(Integer boardSeq, Pageable pageable);
	// boardCode 기준으로 페이징 조회
    Page<KnowledgePost> findByKnowledgeBoardBoardCode(String boardCode, Pageable pageable);
    List<KnowledgePost> findByKnowledgeBoardBoardCode(String boardCode);
    
    
	 
}
	

