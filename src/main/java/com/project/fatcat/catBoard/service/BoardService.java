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



//package com.project.fatcat.catBoard.service;
//
//import java.util.Optional;
//
//import org.springframework.stereotype.Service;
//
//import com.project.fatcat.catBoard.repository.BoardRepository;
//import com.project.fatcat.entity.KnowledgeBoard;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class BoardService {
//
//    private final BoardRepository boardRepository;
//
//    // ⭐ 기존 메서드 (Integer ID로 찾기) - 오류의 원인: DB에 해당 ID의 데이터가 없었거나 불일치
//    public KnowledgeBoard getBoard(Integer boardSeq) {
//        return boardRepository.findById(boardSeq)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다. seq=" + boardSeq));
//    }
//    
//    // ⭐ 추가/수정: String 타입의 boardCode로 게시판을 찾는 메서드 추가
//    public KnowledgeBoard getBoard(String boardCode) {
//        // KnowledgeBoard를 boardCode (예: "1", "2")로 찾는 Repository 메서드가 필요합니다.
//        return boardRepository.findByBoardCode(boardCode)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다. code=" + boardCode));
//    }
//}
