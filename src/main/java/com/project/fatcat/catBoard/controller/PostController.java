package com.project.fatcat.catBoard.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.project.fatcat.catBoard.form.CommentForm;
import com.project.fatcat.catBoard.form.PostForm;
import com.project.fatcat.catBoard.service.BoardService;
import com.project.fatcat.catBoard.service.CommentService;
import com.project.fatcat.catBoard.service.PostService;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgeComment;
import com.project.fatcat.entity.KnowledgePost;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

//    private final PostService postService;
//    private final BoardService boardService;
//    private final CommentService commentService;
//    private final UserService userService;
//
//
//
//    
//    
////    @GetMapping("/list")
////	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {
////		Page<KnowledgePost> paging = this.postService.postList(page);
////		model.addAttribute("paging", paging);
////		return "post_list";
////	}
//
//	@GetMapping("/detail/{postSeq}")
//	public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, CommentForm commentForm) {
//
//		KnowledgePost post = this.postService.getPost(postSeq);
//		model.addAttribute("post", post);
//		return "post_detail";
//	}
//
//	@GetMapping("/create")
//	// @PreAuthorize("isAuthenticated()") -> 로그인 상태에서만 동작하게
////	@PreAuthorize("isAuthenticated()")
//	public String create(PostForm postForm) {
//		return "post_form";
//	}
//
//	@PostMapping("/create/{boardSeq}")
////	@PreAuthorize("isAuthenticated()")
//	public String create(@Valid PostForm postForm, BindingResult bindingResult, Principal principal,@PathVariable("boardSeq") Integer boardSeq) {
//
//		if (bindingResult.hasErrors()) {
//			return "post_form";
//		}
////		SiteUser siteUser = this.userService.getUser(principal.getName());
//		this.postService.createPost(postForm.getPostTitle(), postForm.getPostContent());
//		return String.format("redirect:/post/list/%s", boardSeq);
//	}
//
//	@GetMapping("/modify/{postSeq}")
////	@PreAuthorize("isAuthenticated()")
//	public String questionModify(PostForm postForm, @PathVariable("postSeq") Integer postSeq, Principal principal) {
//
//		KnowledgePost post = this.postService.getPost(postSeq);
//
////		if (!post.getAuthor().getUsername().equals(principal.getName())) {
////			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
////		}
//
//		postForm.setPostTitle(post.getPostTitle());
//		postForm.setPostContent(post.getPostContent());
//		return "post_form";
//	}
//
//	@PostMapping("/modify/{postSeq}")
////	@PreAuthorize("isAuthenticated()")
//	public String modify(@Valid PostForm postForm, BindingResult bindingResult, Principal principal,
//			@PathVariable("postSeq") Integer postSeq) {
//
//		if (bindingResult.hasErrors()) {
//			return "post_form";
//		}
//
//		KnowledgePost post = this.postService.getPost(postSeq);
//
////		if (!question.getAuthor().getUsername().equals(principal.getName())) {
////			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다");
////		}
//
//		this.postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
//
//		return String.format("redirect:/post/detail/%s", postSeq);
//	}
//
//	@GetMapping("/delete/{postSeq}")
////	@PreAuthorize("isAuthenticated()")
//	public String postDelete(Principal principal, @PathVariable("postSeq") Integer postSeq) {
//
//		KnowledgePost post = this.postService.getPost(postSeq);
//
////		if (!question.getAuthor().getUsername().equals(principal.getName())) {
////			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다");
////		}
//
//		this.postService.delete(post);
//		return "redirect:/";
//
//	}
//    
//    
//	
//	
//	
//	// 게시판별 글 목록
//	@GetMapping("/list/{boardSeq}")
//	public String list(@PathVariable("boardSeq") Integer boardSeq,
////			@RequestParam(name = "boardCode", required = false) String boardCode,
//	                          @RequestParam(value = "page", defaultValue = "0") int page,
//	                          Model model) {
//	    // 게시판 정보 가져오기
//	    KnowledgeBoard board = boardService.getBoard(boardSeq);
//	    model.addAttribute("board", board);
//
//	    // 해당 게시판의 글 페이징 처리
//	    Page<KnowledgePost> paging = postService.getListByBoard(boardSeq, page);
//	    model.addAttribute("paging", paging);
//	    
//	    
//	    
////	    if(boardCode == null) boardCode = "1"; // 기본 코드
////
////	    List<KnowledgeBoard> boards = boardService.getBoard();
////	    model.addAttribute("boards", boards);
//
//	    // boardCode → 이름 매핑
//	    Map<Integer, String> boardNames = new HashMap<>();
//	    boardNames.put(1, "수의사에게 질문하기");
//	    boardNames.put(2, "냥꿀팁");
//
//	    model.addAttribute("currentBoardName", boardNames.get(boardSeq));
//
////	    Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
//	    // Page<KnowledgePost> posts = postService.getPostsByBoardCode(boardCode, page);
//	    // model.addAttribute("posts", posts);
//	    
//
//	    return "post_list";
//	}
	
	
	
    
    
    
	  private final PostService postService;
	    private final BoardService boardService;
	    private final CommentService commentService;

	    // 게시글 상세
	    @GetMapping("/detail/{postSeq}")
	    public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, CommentForm commentForm) {
	        KnowledgePost post = this.postService.getPost(postSeq);
	        model.addAttribute("post", post);
	        return "post_detail";
	    }

	    // 게시글 작성 폼
	    @GetMapping("/create/{boardSeq}")
	    public String createForm(@PathVariable("boardSeq") Integer boardSeq, PostForm postForm, Model model) {
	        KnowledgeBoard board = boardService.getBoard(boardSeq);
	        model.addAttribute("board", board);
	        return "post_form";
	    }

	    // 게시글 작성 처리
	    @PostMapping("/create/{boardSeq}")
	    public String create(@Valid PostForm postForm, BindingResult bindingResult,
	                         Principal principal, @PathVariable("boardSeq") Integer boardSeq) {
	        if (bindingResult.hasErrors()) {
	            return "post_form";
	        }
	        postService.createPost(postForm.getPostTitle(), postForm.getPostContent());
	        return String.format("redirect:/post/list/%s", boardSeq);
	    }

	    // 게시글 수정 폼
	    @GetMapping("/modify/{postSeq}")
	    public String modifyForm(PostForm postForm, @PathVariable("postSeq") Integer postSeq, Principal principal) {
	        KnowledgePost post = postService.getPost(postSeq);
	        postForm.setPostTitle(post.getPostTitle());
	        postForm.setPostContent(post.getPostContent());
	        return "post_form";
	    }

	    // 게시글 수정 처리
	    @PostMapping("/modify/{postSeq}")
	    public String modify(@Valid PostForm postForm, BindingResult bindingResult,
	                         Principal principal, @PathVariable("postSeq") Integer postSeq) {
	        if (bindingResult.hasErrors()) {
	            return "post_form";
	        }
	        KnowledgePost post = postService.getPost(postSeq);
	        postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
	        return String.format("redirect:/post/detail/%s", postSeq);
	    }

	    // 게시글 삭제
	    @GetMapping("/delete/{postSeq}")
	    public String postDelete(Principal principal, @PathVariable("postSeq") Integer postSeq) {
	        KnowledgePost post = postService.getPost(postSeq);
	        postService.delete(post);
	        return "redirect:/";
	    }

	    // 게시판별 글 목록
	    @GetMapping("/list/{boardSeq}")
	    public String list(@PathVariable("boardSeq") Integer boardSeq,
	                       @RequestParam(value = "page", defaultValue = "0") int page,
	                       Model model) {

	        KnowledgeBoard board = boardService.getBoard(boardSeq);
	        model.addAttribute("board", board);

	        Page<KnowledgePost> paging = postService.getListByBoard(boardSeq, page);
	        model.addAttribute("paging", paging);

	        // boardSeq → 이름 매핑
	        Map<Integer, String> boardNames = new HashMap<>();
	        boardNames.put(1, "수의사에게 질문하기");
	        boardNames.put(2, "냥꿀팁");

	        model.addAttribute("currentBoardName", boardNames.get(boardSeq));

	        return "post_list";
	    }
    
    
    
    
    
    
//    @GetMapping("/create")
//    public String showPostForm(Model model) {
//        model.addAttribute("postForm", new PostForm());
//        model.addAttribute("boards", boardService.getBoard());
//        return "post_form";
//    }
//
//   @PostMapping("/create")
//  public String createPost(@ModelAttribute PostForm postForm) {
//        postService.createPost(postForm);
//        return "redirect:/post/list";
//    }
//    
//    
//    
//    
//    
//    
//    /** 게시글 목록 **/
//    @GetMapping("/list")
//    public String listPosts(
//            @RequestParam(value = "boardCode", defaultValue = "all") String boardCode,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            Model model) {
//
//        Page<KnowledgePost> posts = null;
//
//        if ("1".equals(boardCode)) {
//            posts = postService.getPostsByBoardCode(boardCode, page); // 특정 게시판 글
//        }else if("2".equals(boardCode)) {
//        	posts = postService.getPostsByBoardCode(boardCode, page); // 특정 게시판 글
//        }
//
//        model.addAttribute("posts", posts);
//        model.addAttribute("boardCode", boardCode);
//
//        return "board_list"; // board_list.html 사용
//    }
//    
//    @GetMapping("/detail/{seq}")
//    public String detail(@PathVariable("seq") Integer seq, Model model) {
//        KnowledgePost post = postService.getPost(seq); // service에서 불러오기
//        if (post.getKnowledgeBoard() == null) {
//            System.out.println("⚠️ Board is null for post: " + seq);
//        }
//        model.addAttribute("post", post);
//        model.addAttribute("commentForm", new CommentForm());
//        return "post_detail";
//    }
//    
//    /** 게시글 상세 **/
//    @GetMapping("/post/{seq}")
//    public String postDetail(@PathVariable("seq") Integer seq, Model model) {
//        Optional<KnowledgePost> optionalPost = postService.getPostOptional(seq);
//        if (optionalPost.isEmpty()) {
//            model.addAttribute("errorMessage", "해당 글이 없습니다.");
//            return "error_page";
//        }
//
//        model.addAttribute("post", optionalPost.get());
//        return "post_detail"; // ✅ templates/post_detail.html 렌더링
//    }
//
//    
//    
//    /** 게시글 작성 폼 **/
//    /*
//    @GetMapping("/create")
//    public String createPostForm(@RequestParam(value = "boardCode", defaultValue = "all") String boardCode,
//                                 Model model) {
//        model.addAttribute("boardCode", boardCode);
//        model.addAttribute("post", new PostForm()); // <- 여기 중요
//        return "post_form";
//    }
//*/
//
//
//    /** 게시글 작성 처리 **/
//    /*
//    @PostMapping("/create")
//    public String createPost(@Valid PostForm postForm,
//                             BindingResult bindingResult,
//                             @RequestParam("boardCode") String boardCode) {
//
//        if (bindingResult.hasErrors()) {
//            return "post_form";
//        }
//
//        postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardCode);
//
//        return "redirect:/post/list?boardCode=" + boardCode;
//    }
//    */
//
//    public String modifyPostForm(@PathVariable Integer seq, Model model) {
//        Optional<KnowledgePost> optionalPost = postService.getPostOptional(seq);
//        if (optionalPost.isEmpty()) {
//            model.addAttribute("errorMessage", "해당 글이 없습니다.");
//            return "error_page";
//        }
//
//        KnowledgePost post = optionalPost.get();
//        PostForm postForm = new PostForm();
//        postForm.setPostTitle(post.getPostTitle());
//        postForm.setPostContent(post.getPostContent());
//
//        model.addAttribute("post", postForm); // ✅ 반드시 모델에 담아야 함
//        return "post_form";
//    }
//
//    @PostMapping("/modify/{seq}")
//    public String modifyPost(@PathVariable Integer seq,
//                             @Valid PostForm postForm,
//                             BindingResult bindingResult,
//                             Principal principal,
//                             Model model) {
//
//        if (bindingResult.hasErrors()) {
//            return "post_form";
//        }
//
//        Optional<KnowledgePost> optionalPost = postService.getPostOptional(seq);
//        if (optionalPost.isEmpty()) {
//            model.addAttribute("errorMessage", "해당 글이 없습니다.");
//            return "error_page";
//        }
//
//        KnowledgePost post = optionalPost.get();
//        postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
//        return "redirect:/post/post/" + seq;
//    }
//
//    @GetMapping("/delete/{seq}")
//    public String deletePost(@PathVariable Integer seq, Principal principal, Model model) {
//        Optional<KnowledgePost> optionalPost = postService.getPostOptional(seq);
//        if (optionalPost.isEmpty()) {
//            model.addAttribute("errorMessage", "해당 글이 없습니다.");
//            return "error_page";
//        }
//
//        postService.delete(optionalPost.get());
//        return "redirect:/post/list?boardCode=all";
//    }

}
