
package com.project.fatcat.catBoard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.catBoard.repository.BoardRepository;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.repository.UserRepository;
import com.project.fatcat.users.service.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

       if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("로그인한 사용자만 글을 작성할 수 있습니다.");
        }
        
        Object principal = auth.getPrincipal();
        Integer userSeq;
        
        if (principal instanceof CustomUserDetails userDetails) {
            
            userSeq = userDetails.getUser().getUserSeq();
        } else {
            throw new IllegalStateException("인증된 사용자 정보(CustomUserDetails)를 찾을 수 없습니다.");
        }
        
        // 3. userSeq로 User 엔티티 조회 (최신 DB 정보 사용)
        return userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("DB에서 사용자의 정보를 찾을 수 없습니다. (userSeq: " + userSeq + ")"));
    }

    
    // boardSeq로 글 가져오기
    public List<KnowledgePost> getPostsByBoardSeq(String boardCode) {
        return postRepository.findByKnowledgeBoardBoardCode(boardCode);
    }
    
    
    public Page<KnowledgePost> getPostsByBoardSeq(Integer boardSeq, Pageable pageable) {
        return postRepository.findByKnowledgeBoardBoardSeq(boardSeq, pageable);
    }

    
	public KnowledgePost getPost(Integer postSeq) {

		Optional<KnowledgePost> qu = this.postRepository.findById(postSeq);

		if (qu.isPresent()) {
			return qu.get();
		} else {
			throw new DataNotFoundException("질문을 찾을 수 없습니다.");
		}
	}
	
	public Page<KnowledgePost> getPostsByBoardCode(String boardCode, Pageable pageable) {
	    // findByKnowledgeBoardBoardCode 메서드가 Page<KnowledgePost>를 반환한다고 가정합니다.
	    return postRepository.findByKnowledgeBoardBoardCode(boardCode, pageable);
	}
	
    
    @Transactional 
	public void createPostWithBoardCode(String title, String content, String boardCode) {
		
	    // 1. 게시판 정보 확인
	    KnowledgeBoard board = boardRepository.findByBoardCode(boardCode)
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다: " + boardCode));

	    // 2. 현재 로그인된 사용자 정보 가져오기 (로그인 강제)
	    User currentUser = getLoggedInUser(); 

	    // 3. 게시글 엔티티 생성 및 저장
	    KnowledgePost post = new KnowledgePost();
	    post.setPostTitle(title);
	    post.setPostContent(content);
	    post.setUser(currentUser); 
	    post.setKnowledgeBoard(board);
	    post.setCreateDate(LocalDateTime.now());

	    postRepository.save(post);
	}
	
    @Transactional
	public void modify(KnowledgePost post, String postTitle, String postContent) {
		post.setPostTitle(postTitle);
		post.setPostContent(postContent);
		post.setUpdateDate(LocalDateTime.now());

		this.postRepository.save(post);
	}

    @Transactional
	public void delete(KnowledgePost post) {
		this.postRepository.delete(post);
	}
}
