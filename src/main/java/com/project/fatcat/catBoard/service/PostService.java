//package com.project.fatcat.catBoard.service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//import com.project.fatcat.DataNotFoundException;
//import com.project.fatcat.catBoard.repository.BoardRepository;
//import com.project.fatcat.catBoard.repository.PostRepository;
//import com.project.fatcat.entity.KnowledgeBoard;
//import com.project.fatcat.entity.KnowledgePost;
//import com.project.fatcat.entity.User;
//import com.project.fatcat.users.repository.UserRepository;
//import com.project.fatcat.users.service.CustomUserDetails;
//
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class PostService {
//
//    private final PostRepository postRepository;
//    private final BoardService boardService;
//    private final BoardRepository boardRepository;
//    private final UserRepository userRepository;
//
//    
//    // boardSeq로 글 가져오기
//    public List<KnowledgePost> getPostsByBoardSeq(String boardCode) {
//        return postRepository.findByKnowledgeBoardBoardCode(boardCode);
//    }
//    
//    
//    public Page<KnowledgePost> getPostsByBoardSeq(Integer boardSeq, Pageable pageable) {
//        return postRepository.findByKnowledgeBoardBoardSeq(boardSeq, pageable);
//    }
//
//    
//	public KnowledgePost getPost(Integer postSeq) {
//
//		Optional<KnowledgePost> qu = this.postRepository.findById(postSeq);
//
//		if (qu.isPresent()) {
//			return qu.get();
//		} else {
//			throw new DataNotFoundException("질문을 찾을 수 없습니다.");
//		}
//
//	}
//
////	public Page<KnowledgePost> postList(int page) {
////		List<Sort.Order> sorts = new ArrayList<>();
////		sorts.add(Sort.Order.desc("createDate"));
////		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
////		return this.postRepository.findAll(pageable);
////	}
//
////	public void createPost(String postTitle, String postContent) {
////
////		KnowledgePost post = new KnowledgePost();
////		post.setPostContent(postContent);
////		post.setPostTitle(postTitle);
////		post.setCreateDate(LocalDateTime.now());
//////		post.setAuthor(user);
////		this.postRepository.save(post);
////	}
//
//	public void modify(KnowledgePost post, String postTitle, String postContent) {
//		post.setPostTitle(postTitle);
//		post.setPostContent(postContent);
//		post.setUpdateDate(LocalDateTime.now());
//
//		this.postRepository.save(post);
//	}
//
//	public void delete(KnowledgePost post) {
//		this.postRepository.delete(post);
//	}
//	
//	
//	
////	public Page<KnowledgePost> getListByBoard(String boardCode, int page) {
////	    Pageable pageable = PageRequest.of(page, 5);
////	    return (Page<KnowledgePost>) postRepository.findByKnowledgeBoardBoardCode(boardCode, pageable);
////	}
////	
//	
//	public Page<KnowledgePost> getPostsByBoardCode(String boardCode, Pageable pageable) {
//	    return (Page<KnowledgePost>) postRepository.findByKnowledgeBoardBoardCode(boardCode, pageable);
//	}
//	
//	public void createPostWithBoardCode(String title, String content, String boardCode, User user) {
//		
//	    KnowledgeBoard board = boardRepository.findByBoardCode(boardCode)
//	            .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다"));
//
//	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new IllegalStateException("로그인 필요");
//        }
//
//        Object principal = auth.getPrincipal();
//        String userName; 
//        
//        if (principal instanceof CustomUserDetails userDetails) {
//        	userName = userDetails.getUser().getUserName();
//        } else {
//            throw new IllegalStateException("인증된 사용자 정보 없음");
//        }
//
//	    
//	    KnowledgePost post = new KnowledgePost();
//	    post.setPostTitle(title);
//	    post.setPostContent(content);
//	    post.setUser(user);
//	    post.setKnowledgeBoard(board);
//	    post.setCreateDate(LocalDateTime.now());
//
//	    postRepository.save(post);
//	}
//	
//	
////	public KnowledgePost getPostWithComments(Integer postSeq) {
////	    return postRepository.findByIdWithComments(postSeq)
////	        .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음"));
////	}
// 
//
//
//	
// 
//}





package com.project.fatcat.catBoard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

//	public Page<KnowledgePost> postList(int page) {
//		List<Sort.Order> sorts = new ArrayList<>();
//		sorts.add(Sort.Order.desc("createDate"));
//		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
//		return this.postRepository.findAll(pageable);
//	}

//	public void createPost(String postTitle, String postContent) {
//
//		KnowledgePost post = new KnowledgePost();
//		post.setPostContent(postContent);
//		post.setPostTitle(postTitle);
//		post.setCreateDate(LocalDateTime.now());
////		post.setAuthor(user);
//		this.postRepository.save(post);
//	}

	public void modify(KnowledgePost post, String postTitle, String postContent) {
		post.setPostTitle(postTitle);
		post.setPostContent(postContent);
		post.setUpdateDate(LocalDateTime.now());

		this.postRepository.save(post);
	}

	public void delete(KnowledgePost post) {
		this.postRepository.delete(post);
	}
	
	
	
//	public Page<KnowledgePost> getListByBoard(String boardCode, int page) {
//	    Pageable pageable = PageRequest.of(page, 5);
//	    return (Page<KnowledgePost>) postRepository.findByKnowledgeBoardBoardCode(boardCode, pageable);
//	}
//	
	
	public Page<KnowledgePost> getPostsByBoardCode(String boardCode, Pageable pageable) {
	    return (Page<KnowledgePost>) postRepository.findByKnowledgeBoardBoardCode(boardCode, pageable);
	}
	
	// User user 인자를 제거했습니다.
	public void createPostWithBoardCode(String title, String content, String boardCode) {
		
	    KnowledgeBoard board = boardRepository.findByBoardCode(boardCode)
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다"));

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            // 이 코드는 컨트롤러에서 @PreAuthorize 등으로 막는 것이 더 일반적이지만, 서비스에서 막는 것도 유효합니다.
            throw new IllegalStateException("로그인 필요"); 
        }

        Object principal = auth.getPrincipal();
        User currentUser; 
        
        // 1. 인증된 사용자 정보를 CustomUserDetails에서 추출합니다.
        if (principal instanceof CustomUserDetails userDetails) {
            
            // 2. CustomUserDetails 객체를 통해 DB에서 User 엔티티를 안전하게 조회합니다.
            // (userRepository.findById()는 Integer를 인자로 받으므로, userDetails.getUser().getUserSeq()를 가정했습니다.)
            // 만약 CustomUserDetails에서 User 객체를 직접 꺼낼 수 있다면 아래의 userRepository 조회는 생략 가능합니다.
            
            Integer userSeq = userDetails.getUser().getUserSeq(); 
            currentUser = userRepository.findById(userSeq)
                    .orElseThrow(() -> new IllegalStateException("인증된 사용자의 정보가 DB에 없습니다."));

        } else {
            throw new IllegalStateException("인증된 사용자 정보 없음");
        }

	    
	    KnowledgePost post = new KnowledgePost();
	    post.setPostTitle(title);
	    post.setPostContent(content);
	    post.setUser(currentUser); // 3. DB에서 안전하게 조회한 currentUser를 사용합니다.
	    post.setKnowledgeBoard(board);
	    post.setCreateDate(LocalDateTime.now());

	    postRepository.save(post);
	}
	
	
//	public KnowledgePost getPostWithComments(Integer postSeq) {
//	    return postRepository.findByIdWithComments(postSeq)
//	        .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음"));
//	}
 
	
 
}
