package com.project.fatcat.catBoard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.KnowledgeComment;
import com.project.fatcat.entity.KnowledgePost;


public interface CommentRepository extends JpaRepository<KnowledgeComment, Integer> {
	
//	 List<KnowledgeComment> findByKnowledgePostOrderByCreateDateAsc(KnowledgePost post);

	}
