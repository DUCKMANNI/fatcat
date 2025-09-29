//
//
//package com.project.fatcat.catBoard.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.project.fatcat.catBoard.form.CommentForm;
//import com.project.fatcat.catBoard.form.PostForm;
//
//import com.project.fatcat.catBoard.repository.PostRepository;
//
//import com.project.fatcat.catBoard.service.BoardService;
//import com.project.fatcat.catBoard.service.PostService;
//import com.project.fatcat.entity.KnowledgeBoard;
//import com.project.fatcat.entity.KnowledgePost;
//import com.project.fatcat.entity.User;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/post")
//public class PostController {
//
//	private final PostService postService;
//	private final BoardService boardService;
//
//	// 게시판별 글 목록 (페이징 포함)
//	@GetMapping("/list/{boardCode}")
//	public String list(@PathVariable("boardCode") String boardCode,
//			@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
//
//		// 게시판 이름 매핑
//		Map<String, String> boardNames = new HashMap<>();
//		boardNames.put("1", "수의사에게 질문하기");
//		boardNames.put("2", "냥꿀팁");
//		model.addAttribute("boardName", boardNames.get(boardCode));
//
//		// 페이징 설정 (페이지당 10개, 최신순)
//		Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
//		Page<KnowledgePost> paging = postService.getPostsByBoardCode(boardCode, pageable);
//		model.addAttribute("paging", paging);
//
//		return "catBoard/post_list";
//	}
//
//	// 게시글 상세
//	@GetMapping("/detail/{postSeq}")
//	public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, 
//							PostForm postForm, CommentForm commentForm) {
//		KnowledgePost post = postService.getPost(postSeq);
//		model.addAttribute("post", post);
//		
//		// 템플릿이 게시글 인라인 수정 폼을 로드할 수 있도록 postForm 초기화
//		// **필수:** 상세 페이지 진입 시 postForm에 현재 post 내용을 채워줍니다.
//		postForm.setPostTitle(post.getPostTitle());
//		postForm.setPostContent(post.getPostContent());
//		
//		return "catBoard/post_detail";
//	}
//
//	// 게시글 작성 폼
//	@GetMapping("/create/{boardCode}")
//	public String createForm(@PathVariable("boardCode") Integer boardCode, PostForm postForm, Model model) {
//		KnowledgeBoard board = boardService.getBoard(boardCode);
//
//		model.addAttribute("board", board);
//		return "catBoard/post_form";
//	}
//
//	// 게시글 작성 처리
//	@PostMapping("/create/{boardCode}")
//	public String create(@Valid PostForm postForm, BindingResult bindingResult,
//			@PathVariable("boardCode") String boardCode, User user) {
//
//		if (bindingResult.hasErrors()) {
//			// 게시판 정보가 필요하다면 이 부분도 수정 필요
//			return "catBoard/post_form";
//		}
//		
//		postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardCode, user);
//		return String.format("redirect:/post/list/%s", boardCode);
//	}
//
//	// 게시글 수정 폼
//		@GetMapping("/modify/{postSeq}")
//		public String modifyForm(Model model, PostForm postForm, @PathVariable("postSeq") Integer postSeq) {
//			KnowledgePost post = postService.getPost(postSeq);
//			postForm.setPostTitle(post.getPostTitle());
//			postForm.setPostContent(post.getPostContent());
//			model.addAttribute("postSeq", postSeq);
//			return "catBoard/post_modify";
//		}
//
//		// 게시글 수정 처리
//		@PostMapping("/modify/{postSeq}")
//		public String modify(@Valid PostForm postForm, BindingResult bindingResult,
//				@PathVariable("postSeq") Integer postSeq) {
//			
//			if (bindingResult.hasErrors()) {
//				return "catBoard/post_modify";
//			}
//			
//			
//			KnowledgePost post = this.postService.getPost(postSeq);
////			Integer boardCode = post.getKnowledgeBoard().getBoardSeq();
//			this.postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
//			return String.format("redirect:/post/detail/%s" , postSeq);
//		}
//
//
//	// 게시글 삭제
//	@GetMapping("/delete/{postSeq}")
//	public String postDelete(@PathVariable("postSeq") Integer postSeq) {
//		KnowledgePost post = postService.getPost(postSeq);
//		Integer boardCode = post.getKnowledgeBoard().getBoardSeq();
//		postService.delete(post);
//		return "redirect:/post/list/" + boardCode;
//	}
//}



package com.project.fatcat.catBoard.controller;

import java.util.HashMap;
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
import com.project.fatcat.entity.User; // User 엔티티는 클래스에서 직접 사용되지 않으므로 사실상 불필요한 import입니다.

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

		// 게시판 이름 매핑
		Map<String, String> boardNames = new HashMap<>();
		boardNames.put("1", "수의사에게 질문하기");
		boardNames.put("2", "냥꿀팁");
		// boardCode가 String이므로 Key도 String으로 처리하는 것이 좋습니다.
		model.addAttribute("boardName", boardNames.get(boardCode));

		// 페이징 설정 (페이지당 10개, 최신순)
		Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
		Page<KnowledgePost> paging = postService.getPostsByBoardCode(boardCode, pageable);
		model.addAttribute("paging", paging);

		return "catBoard/post_list";
	}

	// 게시글 상세
	@GetMapping("/detail/{postSeq}")
	public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, 
							PostForm postForm, CommentForm commentForm) {
		KnowledgePost post = postService.getPost(postSeq);
		model.addAttribute("post", post);
		
		// 템플릿이 게시글 인라인 수정 폼을 로드할 수 있도록 postForm 초기화
		// **필수:** 상세 페이지 진입 시 postForm에 현재 post 내용을 채워줍니다.
		postForm.setPostTitle(post.getPostTitle());
		postForm.setPostContent(post.getPostContent());
		
		return "catBoard/post_detail";
	}

	// 게시글 작성 폼
	// 참고: URL PathVariable 타입이 String으로 넘어오는데, Integer로 받는 것이 일관성에 좋습니다.
	// 현재 DB BoardSeq가 Integer라고 가정하고 PostService와 일치시킵니다.
	@GetMapping("/create/{boardCode}")
	public String createForm(@PathVariable("boardSeq") Integer boardSeq, PostForm postForm, Model model) {
		KnowledgeBoard board = boardService.getBoard(boardSeq);

		model.addAttribute("board", board);
		return "catBoard/post_form";
	}

	// 게시글 작성 처리
	@PostMapping("/create/{boardCode}")
	public String create(@Valid PostForm postForm, BindingResult bindingResult,
			@PathVariable("boardCode") String boardCode) { // 1. User user 인자를 제거했습니다.

		if (bindingResult.hasErrors()) {
			// Validation 실패 시, boardCode에 해당하는 게시판 정보를 다시 뷰에 전달해야 폼이 정상 작동합니다.
			// Integer.parseInt(boardCode)를 사용하거나 boardCode를 String으로 받는 BoardService 메서드를 사용하는 것이 좋습니다.
			// 현재는 간단히 오류가 나지 않도록 Integer로 가정하고 보드를 다시 로드합니다.
			try {
				KnowledgeBoard board = boardService.getBoard(Integer.parseInt(boardCode));
				// board가 없을 경우 IllegalArgumentException이 발생할 수 있으나, 이미 PostService에서 처리되고 있습니다.
			} catch (NumberFormatException e) {
				// boardCode가 숫자가 아닌 경우 처리 (선택 사항)
				// return "error_page";
			}
			return "catBoard/post_form";
		}
		
		// 2. 서비스 호출 시 User user 인자를 제거했습니다.
		// 서비스 레이어에서 SecurityContextHolder를 통해 안전하게 사용자 정보를 가져옵니다.
		postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardCode);
		
		return String.format("redirect:/post/list/%s", boardCode);
	}

	// 게시글 수정 폼
	@GetMapping("/modify/{postSeq}")
	public String modifyForm(Model model, PostForm postForm, @PathVariable("postSeq") Integer postSeq) {
		KnowledgePost post = postService.getPost(postSeq);
		
		// TODO: 현재 로그인된 사용자와 게시글 작성자가 일치하는지 확인하는 로직 (권한 체크)이 필요합니다.

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
		// TODO: 현재 로그인된 사용자와 게시글 작성자가 일치하는지 확인하는 로직 (권한 체크)이 필요합니다.
		
		this.postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
		return String.format("redirect:/post/detail/%s" , postSeq);
	}


	// 게시글 삭제
	@GetMapping("/delete/{postSeq}")
	public String postDelete(@PathVariable("postSeq") Integer postSeq) {
		KnowledgePost post = postService.getPost(postSeq);
		
		// TODO: 현재 로그인된 사용자와 게시글 작성자가 일치하는지 확인하는 로직 (권한 체크)이 필요합니다.
		
		// 게시판 Sequence를 가져와서 리다이렉트
		Integer boardSeq = post.getKnowledgeBoard().getBoardSeq();
		postService.delete(post);
		
		// String.valueOf(boardSeq)로 변환하여 리다이렉트 (PathVariable이 String일 경우)
		return "redirect:/post/list/" + String.valueOf(boardSeq);
	}
}
