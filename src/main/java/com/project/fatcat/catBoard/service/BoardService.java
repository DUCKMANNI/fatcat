package com.project.fatcat.catboard.service;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.project.fatcat.catboard.repository.BoardRepository;
import com.project.fatcat.catboard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	  private final BoardRepository boardRepository;
	  private final PostRepository postRepository;
	  

	    public List<KnowledgeBoard> getBoard() {
	        return boardRepository.findAll(); // DB에서 모든 게시판 가져오기
	    }

	    // 게시판 id로 게시판 가져오기
	    public KnowledgeBoard getBoard(Integer boardSeq) {
	        return boardRepository.findById(boardSeq)
	                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다. seq=" + boardSeq));
	    }

	

	
}
