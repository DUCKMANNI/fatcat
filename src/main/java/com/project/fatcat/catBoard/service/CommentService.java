////package com.project.fatcat.catBoard.service;
////
////import java.time.LocalDateTime;
////import java.util.Optional;
////
////import org.springframework.stereotype.Service;
////
////import com.project.fatcat.DataNotFoundException;
////import com.project.fatcat.catBoard.repository.CommentRepository;
////import com.project.fatcat.catBoard.repository.PostRepository;
////import com.project.fatcat.entity.KnowledgeComment;
////import com.project.fatcat.entity.KnowledgePost;
////
////import lombok.RequiredArgsConstructor;
////
////@Service
////@RequiredArgsConstructor
////public class CommentService {
////
////    private final CommentRepository commentRepository;
////    private final PostRepository postRepository;
////    
////    
////    
////    public void create(KnowledgePost post, String commentContent) {
////
////		KnowledgeComment a = new KnowledgeComment();
////		a.setKnowledgePost(post);
////		a.setCommentContent(commentContent);
////		a.setCreateDate(LocalDateTime.now());
//////		a.setAuthor(author);
////		this.commentRepository.save(a);
////	}
////
////	public KnowledgeComment getComment(Integer commentSeq) {
////
////		Optional<KnowledgeComment> oa = this.commentRepository.findById(commentSeq);
////
////		if (oa.isPresent()) {
////			return oa.get();
////		} else {
////			throw new DataNotFoundException("답변을 찾을 수 없습니다");
////		}
////
////	}
////
////	public void modify(KnowledgeComment comment, String commentContent) {
////
////		comment.setCommentContent(commentContent);
////		comment.setUpdateDate(LocalDateTime.now());
////
////		this.commentRepository.save(comment);
////
////	}
////
////	public void delete(KnowledgeComment comment) {
////		this.commentRepository.delete(comment);
////	}
////    
////    
////    
////    
////    
////
//////    public void create(KnowledgePost post, String content) {
//////        KnowledgeComment comment = KnowledgeComment.builder()
//////                .knowledgePost(post)
//////                .commentContent(content)
//////                .build();  // user 필드는 null
//////        commentRepository.save(comment);
//////    }
//////
//////    public KnowledgeComment getComment(Integer seq) {
//////        return commentRepository.findById(seq)
//////                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));
//////    }
//////
//////    public void modify(KnowledgeComment comment, String content) {
//////        comment.setCommentContent(content);
//////        comment.setUpdateDate(LocalDateTime.now());
//////        commentRepository.save(comment);
//////    }
//////
//////    public void delete(KnowledgeComment comment) {
//////        commentRepository.delete(comment);
//////    }
////}
//
//
//
//package com.project.fatcat.catBoard.service;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import com.project.fatcat.DataNotFoundException;
//import com.project.fatcat.catBoard.repository.CommentRepository;
//import com.project.fatcat.catBoard.repository.PostRepository;
//import com.project.fatcat.entity.KnowledgeComment;
//import com.project.fatcat.entity.KnowledgePost;
//import com.project.fatcat.entity.User; // <--- User 엔티티 import
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//    private final PostRepository postRepository;
//    
//    
//    
//    public void create(KnowledgePost post, String commentContent, User user) { // <--- User author 매개변수 사용
//
//    	
//    	
//    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new IllegalStateException("로그인 필요");
//        }
//    	
//    	Object principal = auth.getPrincipal();
//        Integer userSeq; 
//        
//        if (principal instanceof CustomUserDetails userDetails) {
//        	userSeq = userDetails.getUser().getUserSeq();
//        } else {
//            throw new IllegalStateException("인증된 사용자 정보 없음");
//        }
//
//    	
//    	
//		KnowledgeComment a = new KnowledgeComment();
//		a.setKnowledgePost(post);
//		a.setCommentContent(commentContent);
//		a.setCreateDate(LocalDateTime.now());
//		a.setUser(user);// <--- KnowledgeComment에 작성자(User) 정보 저장
//		this.commentRepository.save(a);
//	}
//
//	public KnowledgeComment getComment(Integer commentSeq) {
//
//		Optional<KnowledgeComment> oa = this.commentRepository.findById(commentSeq);
//
//		if (oa.isPresent()) {
//			return oa.get();
//		} else {
//			throw new DataNotFoundException("답변을 찾을 수 없습니다");
//		}
//
//	}
//
//	public void modify(KnowledgeComment comment, String commentContent) {
//
//		comment.setCommentContent(commentContent);
//		comment.setUpdateDate(LocalDateTime.now());
//
//		this.commentRepository.save(comment);
//
//	}
//
//	public void delete(KnowledgeComment comment) {
//		this.commentRepository.delete(comment);
//	}
//    
//    
//    
//    
//    
//
////    public void create(KnowledgePost post, String content) {
////        KnowledgeComment comment = KnowledgeComment.builder()
////                .knowledgePost(post)
////                .commentContent(content)
////                .build();  // user 필드는 null
////        commentRepository.save(comment);
////    }
////
////    public KnowledgeComment getComment(Integer seq) {
////        return commentRepository.findById(seq)
////                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));
////    }
////
////    public void modify(KnowledgeComment comment, String content) {
////        comment.setCommentContent(content);
////        comment.setUpdateDate(LocalDateTime.now());
////        commentRepository.save(comment);
////    }
////
////    public void delete(KnowledgeComment comment) {
////        commentRepository.delete(comment);
////    }
//}



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
    
    
    /**
     * 현재 로그인된 사용자의 User 엔티티를 반환합니다.
     * 로그인되어 있지 않거나, 사용자 정보가 유효하지 않으면 예외를 발생시킵니다.
     * @return 현재 로그인된 User 엔티티
     * @throws IllegalStateException 로그인 상태가 아니거나 사용자 정보 추출에 실패했을 때
     */
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
    
    
    
    
    

//    public void create(KnowledgePost post, String content) {
//        KnowledgeComment comment = KnowledgeComment.builder()
//                .knowledgePost(post)
//                .commentContent(content)
//                .build();  // user 필드는 null
//        commentRepository.save(comment);
//    }
//
//    public KnowledgeComment getComment(Integer seq) {
//        return commentRepository.findById(seq)
//                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));
//    }
//
//    public void modify(KnowledgeComment comment, String content) {
//        comment.setCommentContent(content);
//        comment.setUpdateDate(LocalDateTime.now());
//        commentRepository.save(comment);
//    }
//
//    public void delete(KnowledgeComment comment) {
//        commentRepository.delete(comment);
//    }
}
