//package com.project.fatcat.catBoard.controller;
//
//import java.security.Principal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//
//import com.project.fatcat.catBoard.form.CommentForm;
//import com.project.fatcat.catBoard.form.PostForm;
//import com.project.fatcat.catBoard.service.BoardService;
//import com.project.fatcat.catBoard.service.CommentService;
//import com.project.fatcat.catBoard.service.PostService;
//import com.project.fatcat.entity.KnowledgeBoard;
//import com.project.fatcat.entity.KnowledgeComment;
//import com.project.fatcat.entity.KnowledgePost;
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
//	private final CommentService commentService;
//
//	// 게시글 상세
//	@GetMapping("/detail/{postSeq}")
//	public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, CommentForm commentForm) {
//		KnowledgePost post = this.postService.getPost(postSeq);
//		model.addAttribute("post", post);
//		return "catBoard/post_detail";
//	}
//
//	// 게시글 작성 폼
//	@GetMapping("/create/{boardSeq}")
//	public String createForm(@PathVariable("boardSeq") Integer boardSeq, PostForm postForm, Model model) {
//		KnowledgeBoard board = boardService.getBoard(boardSeq);
//		model.addAttribute("board", board);
//		return "catBoard/post_form";
//	}
//
//	// 게시글 작성 처리
//	@PostMapping("/create/{boardSeq}")
//	public String create(@Valid PostForm postForm, BindingResult bindingResult, Principal principal,
//			@PathVariable("boardSeq") Integer boardSeq) {
//		if (bindingResult.hasErrors()) {
//			return "catBoard/post_form";
//		}
//		postService.createPost(postForm.getPostTitle(), postForm.getPostContent());
//		return String.format("redirect:/post/list/%s", boardSeq);
//	}
//
//	// 게시글 수정 폼
//	@GetMapping("/modify/{postSeq}")
//	public String modifyForm(PostForm postForm, @PathVariable("postSeq") Integer postSeq, Principal principal) {
//		KnowledgePost post = postService.getPost(postSeq);
//		postForm.setPostTitle(post.getPostTitle());
//		postForm.setPostContent(post.getPostContent());
//		return "catBoard/post_form";
//	}
//
//	// 게시글 수정 처리
//	@PostMapping("/modify/{postSeq}")
//	public String modify(@Valid PostForm postForm, BindingResult bindingResult, Principal principal,
//			@PathVariable("postSeq") Integer postSeq) {
//		if (bindingResult.hasErrors()) {
//			return "catBoard/post_form";
//		}
//		KnowledgePost post = postService.getPost(postSeq);
//		postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
//		return String.format("redirect:/post/detail/%s", postSeq);
//	}
//
//	// 게시글 삭제
//	@GetMapping("/delete/{postSeq}")
//	public String postDelete(Principal principal, @PathVariable("postSeq") Integer postSeq) {
//		KnowledgePost post = postService.getPost(postSeq);
//		postService.delete(post);
//		return "redirect:/";
//	}
//
//	// 게시판별 글 목록
//	@GetMapping("/list/{boardSeq}")
//	public String list(@PathVariable("boardSeq") Integer boardSeq,
//			@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
//
//		KnowledgeBoard board = boardService.getBoard(boardSeq);
//		model.addAttribute("board", board);
//
//		Page<KnowledgePost> paging = postService.getListByBoard(boardSeq, page);
//		model.addAttribute("paging", paging);
//
//		// boardSeq → 이름 매핑
//		Map<Integer, String> boardNames = new HashMap<>();
//		boardNames.put(1, "수의사에게 질문하기");
//		boardNames.put(2, "냥꿀팁");
//
//		model.addAttribute("currentBoardName", boardNames.get(boardSeq));
//
//		return "catBoard/post_list";
//	}
//
//}



package com.project.fatcat.catBoard.controller;

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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final BoardService boardService;

//    // 게시판별 글 목록
//    @GetMapping("/list/{boardSeq}")
//    public String list(@PathVariable("boardSeq") Integer boardSeq,
//                       @RequestParam(value = "page", defaultValue = "0") int page,
//                       Model model) {
//
//
//
//    	
//    	 // 게시판 이름 세팅
//        Map<Integer, String> boardNames = new HashMap<>();
//        boardNames.put(1, "수의사에게 질문하기");
//        boardNames.put(2, "냥꿀팁");
//        model.addAttribute("currentBoardName", boardNames.get(boardSeq));
//
//        // 게시판 글 가져오기
//        List<KnowledgePost> posts = postService.getPostsByBoardSeq(boardSeq);
//        model.addAttribute("posts", posts);
//
//        return "catBoard/post_list";
//    }
//    
//    @GetMapping("/board/{boardSeq}")
//    public String getPostsByBoard(@PathVariable Integer boardSeq, Model model) {
//
//    	// 게시판 이름
//        Map<Integer, String> boardNames = new HashMap<>();
//        boardNames.put(1, "수의사에게 질문하기");
//        boardNames.put(2, "냥꿀팁");
//        model.addAttribute("currentBoardName", boardNames.get(boardSeq));
//
//        // 게시판 글 가져오기
//        List<KnowledgePost> posts = postService.getPostsByBoardSeq(boardSeq);
//        model.addAttribute("posts", posts);
//
//        return "catBoard/post_list";
//    }
//    
//    // 게시글 상세
//    @GetMapping("/detail/{postSeq}")
//    public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq) {
//        KnowledgePost post = postService.getPost(postSeq);
//        model.addAttribute("post", post);
//        return "catBoard/post_detail";
//    }
//
//    // 게시글 작성 폼
//    @GetMapping("/create/{boardSeq}")
//    public String createForm(@PathVariable("boardSeq") Integer boardSeq, PostForm postForm, Model model) {
//        KnowledgeBoard board = boardService.getBoard(boardSeq);
//        model.addAttribute("board", board);
//        return "catBoard/post_form";
//    }
//
//    // 게시글 작성 처리
//    @PostMapping("/create/{boardSeq}")
//    public String create(@Valid PostForm postForm, BindingResult bindingResult,
//                         @PathVariable("boardSeq") Integer boardSeq) {
//        if (bindingResult.hasErrors()) {
//            return "catBoard/post_form";
//        }
//        postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardSeq);
//        return "redirect:/post/list/" + boardSeq;
//    }
//
//    // 게시글 수정 폼
//    @GetMapping("/modify/{postSeq}")
//    public String modifyForm(PostForm postForm, @PathVariable("postSeq") Integer postSeq) {
//        KnowledgePost post = postService.getPost(postSeq);
//        postForm.setPostTitle(post.getPostTitle());
//        postForm.setPostContent(post.getPostContent());
//        return "catBoard/post_form";
//    }
//
//    // 게시글 수정 처리
//    @PostMapping("/modify/{postSeq}")
//    public String modify(@Valid PostForm postForm, BindingResult bindingResult,
//                         @PathVariable("postSeq") Integer postSeq) {
//        if (bindingResult.hasErrors()) {
//            return "catBoard/post_form";
//        }
//        KnowledgePost post = postService.getPost(postSeq);
//        postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
//        return "redirect:/post/detail/" + postSeq;
//    }
//
//    // 게시글 삭제
//    @GetMapping("/delete/{postSeq}")
//    public String postDelete(@PathVariable("postSeq") Integer postSeq) {
//        KnowledgePost post = postService.getPost(postSeq);
//        postService.delete(post);
//        return "redirect:/post/list/" + post.getKnowledgeBoard().getBoardSeq();
//    }
    
    
    
 // 게시판별 글 목록 (페이징 포함)
    @GetMapping("/list/{boardSeq}")
    public String list(@PathVariable("boardSeq") Integer boardSeq,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {

        // 게시판 이름 매핑
        Map<Integer, String> boardNames = new HashMap<>();
        boardNames.put(1, "수의사에게 질문하기");
        boardNames.put(2, "냥꿀팁");
        model.addAttribute("currentBoardName", boardNames.get(boardSeq));
        model.addAttribute("currentBoardSeq", boardSeq);

        // 페이징 설정 (페이지당 10개, 최신순)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<KnowledgePost> paging = postService.getPostsByBoardSeq(boardSeq, pageable);
        model.addAttribute("paging", paging);

        return "catBoard/post_list";
    }

    // 게시글 상세
    @GetMapping("/detail/{postSeq}")
    public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, PostForm postForm, CommentForm commentForm) {
    	 KnowledgePost post = postService.getPostWithComments(postSeq);
    	    model.addAttribute("post", post);
    	    return "catBoard/post_detail";
    }

    // 게시글 작성 폼
    @GetMapping("/create/{boardSeq}")
    public String createForm(@PathVariable("boardSeq") Integer boardSeq, PostForm postForm, Model model) {
        KnowledgeBoard boards = boardService.getBoard(boardSeq);
        model.addAttribute("boards", boards);
        return "catBoard/post_form";
    }

    // 게시글 작성 처리
    @PostMapping("/create/{boardSeq}")
    public String create(@Valid PostForm postForm, BindingResult bindingResult,
                         @PathVariable("boardSeq") Integer boardSeq) {
        if (bindingResult.hasErrors()) {
            return "catBoard/post_form";
        }
        postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardSeq);
        return String.format("redirect:/post/list/%s", boardSeq);
    }

    // 게시글 수정 폼
    @GetMapping("/modify/{postSeq}")
    public String modifyForm(PostForm postForm, @PathVariable("postSeq") Integer postSeq) {
        KnowledgePost post = postService.getPost(postSeq);
        postForm.setPostTitle(post.getPostTitle());
        postForm.setPostContent(post.getPostContent());
        return "catBoard/post_form";
    }

    // 게시글 수정 처리
    @PostMapping("/modify/{postSeq}")
    public String modify(@Valid PostForm postForm, BindingResult bindingResult,
                         @PathVariable("postSeq") Integer postSeq) {
        if (bindingResult.hasErrors()) {
            return "catBoard/post_form";
        }
        KnowledgePost post = postService.getPost(postSeq);
        postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
        return "redirect:/post/detail/" + postSeq;
    }

    // 게시글 삭제
    @GetMapping("/delete/{postSeq}")
    public String postDelete(@PathVariable("postSeq") Integer postSeq) {
        KnowledgePost post = postService.getPost(postSeq);
        Integer boardSeq = post.getKnowledgeBoard().getBoardSeq();
        postService.delete(post);
        return "redirect:/post/list/" + boardSeq;
    }
}
