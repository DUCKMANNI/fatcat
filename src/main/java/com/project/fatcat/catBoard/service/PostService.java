package com.project.fatcat.catBoard.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.catBoard.repository.BoardRepository;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgePost;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final BoardService boardService;
    private final BoardRepository boardRepository;


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

	public void createPost(String postTitle, String postContent) {

		KnowledgePost post = new KnowledgePost();
		post.setPostContent(postContent);
		post.setPostTitle(postTitle);
		post.setCreateDate(LocalDateTime.now());
//		post.setAuthor(user);
		this.postRepository.save(post);
	}

	public void modify(KnowledgePost post, String postTitle, String postContent) {
		post.setPostTitle(postTitle);
		post.setPostContent(postContent);
		post.setUpdateDate(LocalDateTime.now());

		this.postRepository.save(post);
	}

	public void delete(KnowledgePost post) {
		this.postRepository.delete(post);
	}
	
	
	
	public Page<KnowledgePost> getListByBoard(Integer boardSeq, int page) {
	    Pageable pageable = PageRequest.of(page, 5);
	    return postRepository.findByKnowledgeBoard_BoardSeq(boardSeq, pageable);
	}
	
	
	
	
	
	
	
    
 

//    // Optional 반환
//    public Optional<KnowledgePost> getPostOptional(Integer seq) {
//        return postRepository.findById(seq);
//    }
//
//    // 기존 getPost(seq) 유지하고 예외 던지는 용도
//    public KnowledgePost getPost(Integer seq) {
//        return postRepository.findById(seq)
//                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));
//    }
//
//    public Page<KnowledgePost> postList(int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//        return postRepository.findAll(pageable);
//    }
//
//    public Page<KnowledgePost> getPostsByBoardCode(String boardCode, int page) {
//        Pageable pageable = PageRequest.of(page, 10);
//        return postRepository.findPostsByBoardCode(boardCode, pageable);
//    }
//
//    
//    public void createPost(PostForm postForm) {
//        // boardSeq로 Board 객체 조회
//        KnowledgeBoard board = boardRepository.findById(postForm.getBoardSeq())
//                              .orElseThrow(() -> new IllegalArgumentException("해당 게시판이 없습니다."));
//        
//        KnowledgePost post = new KnowledgePost();
//        post.setPostTitle(postForm.getPostTitle());
//        post.setPostContent(postForm.getPostContent());
//        post.setKnowledgeBoard(board);
//
//        postRepository.save(post);
//    }
//    
//    public void createPostWithBoardCode(String title, String content, String boardCode) {
//        KnowledgePost post = new KnowledgePost();
//        post.setPostTitle(title);
//        post.setPostContent(content);
//        // board 설정 필요
//        // post.setKnowledgeBoard(boardService.getBoard(boardCode));
//        postRepository.save(post);
//    }
//
//    public void modify(KnowledgePost post, String title, String content) {
//        post.setPostTitle(title);
//        post.setPostContent(content);
//        postRepository.save(post);
//    }
//
//    public void delete(KnowledgePost post) {
//        postRepository.delete(post);
//    }

 
}
