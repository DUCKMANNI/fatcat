package com.project.fatcat.catBoard.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.catBoard.form.CommentForm;
import com.project.fatcat.catBoard.service.CommentService;
import com.project.fatcat.catBoard.service.PostService;
import com.project.fatcat.entity.KnowledgeComment;
import com.project.fatcat.entity.KnowledgePost;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

	private final CommentService commentService;
	private final PostService postService;	
	
	
	@PostMapping("/create/{postSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String createComment(Model model, @PathVariable("postSeq") Integer postSeq, @Valid CommentForm commentForm,
			BindingResult bindingResult, Principal principal) {

		KnowledgePost post = this.postService.getPost(postSeq);
//		SiteUser siteUser = this.userService.getUser(principal.getName());

		if (bindingResult.hasErrors()) {
			model.addAttribute("post", post);
			return "catBoard/post_detail";
		}

		this.commentService.create(post, commentForm.getCommentContent());

		return String.format("redirect:/post/detail/%s", postSeq);
	}

	@GetMapping("/modify/{commentSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String commentrModify(CommentForm commentForm, @PathVariable("commentSeq") Integer commentSeq, Principal principal) {

		KnowledgeComment comment = this.commentService.getComment(commentSeq);

//		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
//		}

		commentForm.getCommentContent();
		return "catBoard/comment_form";
	}

	@PostMapping("/modify/{commentSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult, Principal principal,
			@PathVariable("commentSeq") Integer commentSeq) {

		if (bindingResult.hasErrors()) {
			return "catBoard/comment_form";
		}

		KnowledgeComment comment = this.commentService.getComment(commentSeq);
		KnowledgePost post = comment.getKnowledgePost();

//		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
//		}
		this.commentService.modify(comment, commentForm.getCommentContent());

		return String.format("redirect:/post/detail/%s", post.getPostSeq());

	}

	@GetMapping("/delete/{commentSeq}")
//	@PreAuthorize("isAuthenticated()")
	public String answerDelete(Principal principal, @PathVariable("commentSeq") Integer commentSeq) {

		KnowledgeComment comment = this.commentService.getComment(commentSeq);
		KnowledgePost post = comment.getKnowledgePost();

//		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
//			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다");
//		}

		this.commentService.delete(comment);

		return String.format("redirect:/post/detail/%s", post.getPostSeq());
	}
	


}
