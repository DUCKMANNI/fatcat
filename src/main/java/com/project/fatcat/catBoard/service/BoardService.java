package com.project.fatcat.catBoard.service;



import java.util.List;

import org.springframework.stereotype.Service;

import com.project.fatcat.catBoard.repository.BoardRepository;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgeBoard;

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


