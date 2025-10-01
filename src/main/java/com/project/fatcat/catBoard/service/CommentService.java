



package com.project.fatcat.catBoard.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.catBoard.repository.CommentRepository;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgeComment;
import com.project.fatcat.entity.KnowledgePost;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.repository.UserRepository; // <--- UserRepository import
import com.project.fatcat.users.service.CustomUserDetails; // <--- CustomUserDetails import

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository; // <--- UserRepository 의존성 추가
    
    
   
    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

       if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new IllegalStateException("로그인한 사용자만 댓글을 작성할 수 있습니다.");
        }
        
        Object principal = auth.getPrincipal();
        Integer userSeq;
        
        if (principal instanceof CustomUserDetails userDetails) {
            userSeq = userDetails.getUser().getUserSeq();
        } else {
            throw new IllegalStateException("인증된 사용자 정보(CustomUserDetails)를 찾을 수 없습니다.");
        }
        
        // userSeq로 User 엔티티 조회 (최신 DB 정보 사용)
        return userRepository.findById(userSeq)
                .orElseThrow(() -> new IllegalStateException("DB에서 사용자의 정보를 찾을 수 없습니다. (userSeq: " + userSeq + ")"));
    }
    
    
    
    public void create(KnowledgePost post, String commentContent) { // <--- User author 매개변수 제거

		User user = getLoggedInUser(); // <--- 내부에서 로그인 사용자 정보 가져오기

		KnowledgeComment a = new KnowledgeComment();
		a.setKnowledgePost(post);
		a.setCommentContent(commentContent);
		a.setCreateDate(LocalDateTime.now());
		a.setUser(user);  // KnowledgeComment에 작성자(User) 정보 저장
		this.commentRepository.save(a);
	}

	public KnowledgeComment getComment(Integer commentSeq) {

		Optional<KnowledgeComment> oa = this.commentRepository.findById(commentSeq);

		if (oa.isPresent()) {
			return oa.get();
		} else {
			throw new DataNotFoundException("답변을 찾을 수 없습니다");
		}

	}

	public void modify(KnowledgeComment comment, String commentContent) {

		comment.setCommentContent(commentContent);
		comment.setUpdateDate(LocalDateTime.now());

		this.commentRepository.save(comment);

	}

	public void delete(KnowledgeComment comment) {
		this.commentRepository.delete(comment);
	}
    
    

}
