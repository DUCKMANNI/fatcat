
package com.project.fatcat.catBoard.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.project.fatcat.catBoard.form.CommentForm;
import com.project.fatcat.catBoard.service.CommentService;
import com.project.fatcat.catBoard.service.PostService;
import com.project.fatcat.entity.KnowledgeComment;
import com.project.fatcat.entity.KnowledgePost;
// 불필요한 User 및 UserService import 제거

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;
	private final PostService postService;
	// private final UserServiceImpl userServiceImpl; // <--- 삭제됨
	
	
	@PostMapping("/create/{postSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String createComment(Model model, @PathVariable("postSeq") Integer postSeq, @Valid CommentForm commentForm,
			BindingResult bindingResult, Principal principal) {

        // Spring Security 설정을 신뢰하지만, Controller 레벨에서 한번 더 Principal이 null인지 확인합니다.
        if (principal == null) {
            // 이 예외는 SecurityConfig에서 처리되지 않았을 때의 안전망입니다.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "답변 작성은 로그인 후 이용 가능합니다.");
        }
        
		KnowledgePost post = this.postService.getPost(postSeq);
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("post", post);
			return "catBoard/post_detail";
		}

		// CommentService.create(post, content) 시그니처에 맞게 호출
		// 사용자 정보는 CommentService 내부에서 처리합니다.
		this.commentService.create(post, commentForm.getCommentContent()); 

		return String.format("redirect:/post/detail/%s", postSeq);
	}

	@GetMapping("/modify/{commentSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String commentrModify(CommentForm commentForm, @PathVariable("commentSeq") Integer commentSeq, Principal principal) {
        
        // 인증만 확인하고 권한 확인 로직은 Service로 위임합니다.
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "수정은 로그인 후 이용 가능합니다.");
        }

		KnowledgeComment comment = this.commentService.getComment(commentSeq);
		
        // commentService.modify 호출 전, 해당 comment의 작성자와 현재 principal을 비교하여 
        // 수정 권한이 없으면 service에서 ResponseStatusException(HttpStatus.FORBIDDEN)을 던지게 됩니다.

		commentForm.setCommentContent(comment.getCommentContent()); // 폼에 기존 내용 설정
		return "catBoard/comment_form";
	}

	@PostMapping("/modify/{commentSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
			@PathVariable("commentSeq") Integer commentSeq) {

        // 인증만 확인하고 권한은 Service로 위임합니다.
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "수정은 로그인 후 이용 가능합니다.");
        }
        
		if (bindingResult.hasErrors()) {
			return "catBoard/comment_form";
		}

		KnowledgeComment comment = this.commentService.getComment(commentSeq);
		KnowledgePost post = comment.getKnowledgePost();

        // 권한 확인 로직은 CommentService.modify에서 처리됩니다.
		this.commentService.modify(comment, commentForm.getCommentContent());

		return String.format("redirect:/post/detail/%s", post.getPostSeq());

	}

	@GetMapping("/delete/{commentSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String answerDelete(Principal principal, @PathVariable("commentSeq") Integer commentSeq) {

        // 인증만 확인하고 권한은 Service로 위임합니다.
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "삭제는 로그인 후 이용 가능합니다.");
        }
        
		KnowledgeComment comment = this.commentService.getComment(commentSeq);
		KnowledgePost post = comment.getKnowledgePost();

        // 권한 확인 로직은 CommentService.delete에서 처리됩니다.
		this.commentService.delete(comment);

		return String.format("redirect:/post/detail/%s", post.getPostSeq());
	}
	


}
