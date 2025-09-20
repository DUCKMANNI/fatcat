package com.project.fatcat.catboard.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.project.fatcat.DataNotFoundException;
import com.project.fatcat.catboard.repository.CommentRepository;
import com.project.fatcat.catboard.repository.PostRepository;
import com.project.fatcat.entity.KnowledgeComment;
import com.project.fatcat.entity.KnowledgePost;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    
    
    
    public void create(KnowledgePost post, String commentContent) {

		KnowledgeComment a = new KnowledgeComment();
		a.setKnowledgePost(post);
		a.setCommentContent(commentContent);
		a.setCreateDate(LocalDateTime.now());
//		a.setAuthor(author);
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
