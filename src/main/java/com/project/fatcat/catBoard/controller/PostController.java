

package com.project.fatcat.catBoard.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.fatcat.catBoard.form.CommentForm;
import com.project.fatcat.catBoard.form.PostForm;
import com.project.fatcat.catBoard.repository.PostRepository;
import com.project.fatcat.catBoard.service.BoardService;
import com.project.fatcat.catBoard.service.PostService;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;
import com.project.fatcat.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

	private final PostService postService;
	private final BoardService boardService;

	// 게시판별 글 목록 (페이징 포함)
	@GetMapping("/list/{boardCode}")
	public String list(@PathVariable("boardCode") String boardCode,
			@RequestParam(value = "page", defaultValue = "0") int page, Model model) {

//		 // **디버깅용 코드**
//	    // 실제 boardCode 값이 무엇인지 확인하기 위해 강제로 예외를 발생시킵니다.
//	    throw new IllegalArgumentException("boardCode의 실제 값은: [" + boardCode + "] 입니다.");
		
		// 게시판 이름 매핑
		Map<String, String> boardNames = new HashMap<>();
		boardNames.put("1", "수의사에게 질문하기");
		boardNames.put("2", "냥꿀팁");
		model.addAttribute("boardName", boardNames.get(boardCode));

		// 페이징 설정 (페이지당 10개, 최신순)
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
		Page<KnowledgePost> paging = postService.getPostsByBoardCode(boardCode, pageable);
		model.addAttribute("paging", paging);

		return "catBoard/post_list";
	}

	// 게시글 상세
	@GetMapping("/detail/{postSeq}")
	public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, PostForm postForm,
			CommentForm commentForm) {
		KnowledgePost post = postService.getPost(postSeq);
		model.addAttribute("post", post);
		return "catBoard/post_detail";
	}

	// 게시글 작성 폼
	@GetMapping("/create/{boardCode}")
	public String createForm(@PathVariable("boardCode") Integer boardCode, PostForm postForm, Model model) {
		KnowledgeBoard board = boardService.getBoard(boardCode);

		model.addAttribute("board", board);
		return "catBoard/post_form";
	}

	// 게시글 작성 처리
	@PostMapping("/create/{boardCode}")
	public String create(@Valid PostForm postForm, BindingResult bindingResult,
			@PathVariable("boardCode") String boardCode, User user) {

		if (bindingResult.hasErrors()) {
			return "catBoard/post_form";
		}
		
	

		postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardCode, user);
		return String.format("redirect:/post/list/%s", boardCode);
	}

	// 게시글 수정 폼
	@GetMapping("/modify/{postSeq}")
	public String modifyForm(Model model, PostForm postForm, @PathVariable("postSeq") Integer postSeq) {
		KnowledgePost post = postService.getPost(postSeq);
		postForm.setPostTitle(post.getPostTitle());
		postForm.setPostContent(post.getPostContent());
		model.addAttribute("postSeq", postSeq);
		return "catBoard/post_modify";
	}

	// 게시글 수정 처리
	@PostMapping("/modify/{postSeq}")
	public String modify(@Valid PostForm postForm, BindingResult bindingResult,
			@PathVariable("postSeq") Integer postSeq) {
		
		if (bindingResult.hasErrors()) {
			return "catBoard/post_modify";
		}
		
		
		KnowledgePost post = this.postService.getPost(postSeq);
//		Integer boardCode = post.getKnowledgeBoard().getBoardSeq();
		this.postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
		return String.format("redirect:/post/detail/%s" , postSeq);
	}

	// 게시글 삭제
	@GetMapping("/delete/{postSeq}")
	public String postDelete(@PathVariable("postSeq") Integer postSeq) {
		KnowledgePost post = postService.getPost(postSeq);
		Integer boardCode = post.getKnowledgeBoard().getBoardSeq();
		postService.delete(post);
		return "redirect:/post/list/" + boardCode;
	}
}
