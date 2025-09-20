package com.project.fatcat.catboard.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.catboard.repository.BoardRepository;
import com.project.fatcat.catboard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.UserRepository;

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
	
	public void createPostWithBoardCode(String title, String content, String boardCode, User user) {
		
	    KnowledgeBoard board = boardRepository.findByBoardCode(boardCode)
	            .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다"));

//	    User user = new User();
	    Optional<User> ou = this.userRepository.findById(2);
	    if(ou.isPresent());
	    User u = ou.get();
	    
	    
	    KnowledgePost post = new KnowledgePost();
	    post.setPostTitle(title);
	    post.setPostContent(content);
	    post.setUser(u);
	    post.setKnowledgeBoard(board);
	    post.setCreateDate(LocalDateTime.now());

	    postRepository.save(post);
	}
	
	
//	public KnowledgePost getPostWithComments(Integer postSeq) {
//	    return postRepository.findByIdWithComments(postSeq)
//	        .orElseThrow(() -> new IllegalArgumentException("해당 게시글 없음"));
//	}
 


	
 
}
