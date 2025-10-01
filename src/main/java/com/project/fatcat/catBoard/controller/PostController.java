//
//
//package com.project.fatcat.catBoard.controller;
//
//import java.security.Principal; 
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus; 
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.server.ResponseStatusException; 
//
//import com.project.fatcat.catBoard.form.CommentForm;
//import com.project.fatcat.catBoard.form.PostForm;
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
//        model.addAttribute("currentBoardSeq", boardCode);
//        model.addAttribute("boardCode", boardCode); // 템플릿의 '질문 등록' 버튼을 위해 추가
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
//            PostForm postForm, CommentForm commentForm, Principal principal) { // Principal 추가
//KnowledgePost post = postService.getPost(postSeq);
//model.addAttribute("post", post);
//		
//		postForm.setPostTitle(post.getPostTitle());
//		postForm.setPostContent(post.getPostContent());
//		
//		return "catBoard/post_detail";
//	}
//
//	/**
//	 * 게시글 작성 폼을 렌더링합니다. (GET)
//	 * Spring Security 설정에서 이 URL이 보호되고 있다고 가정합니다.
//	 */
//	@GetMapping("/create/{boardCode}")
//	public String createForm(@PathVariable("boardCode") String boardCode, PostForm postForm, Model model, Principal principal) {
//        // Principal 객체가 null이면 Spring Security가 로그인 페이지로 리다이렉트 시켜야 합니다.
//        // 만약 principal이 null인데 이 코드가 실행되면, SecurityConfig에 문제가 있거나 
//        // 인증 오류가 500으로 표시된 것입니다.
//        if (principal == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "글 작성은 로그인 후 이용 가능합니다. (인증 누락)");
//        }
//        
//        // 폼 제출 시 필요한 boardCode를 모델에 담아 전달합니다.
//        model.addAttribute("boardCode", boardCode); 
//		return "catBoard/post_form";
//	}
//
//	/**
//	 * 게시글 작성 처리 (POST)
//	 * Service의 getLoggedInUser()를 호출하여 로그인 상태를 강하게 체크합니다.
//	 */
//	@PostMapping("/create/{boardCode}")
//	public String create(@Valid PostForm postForm, BindingResult bindingResult,
//			@PathVariable("boardCode") String boardCode) { 
//
//		if (bindingResult.hasErrors()) {
//            // 에러 발생 시 boardCode를 다시 전달하여 폼 액션 URL을 유지합니다.
//			return "catBoard/post_form";
//		}
//		
//		try {
//            // Service 내부의 getLoggedInUser()에서 로그인 검증 및 사용자 엔티티 설정을 모두 처리합니다.
//			postService.createPostWithBoardCode(postForm.getPostTitle(), postForm.getPostContent(), boardCode);
//		} catch (IllegalStateException e) {
//            // 로그인 필요 예외 발생 시 401 Unauthorized 반환 (가장 중요한 체크)
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (IllegalArgumentException e) {
//            // 게시판 코드 오류 등 발생 시 400 Bad Request 반환
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
//        }
//		
//		return String.format("redirect:/post/list/%s", boardCode);
//	}
//
//	// 게시글 수정 폼 (작성자 본인만 가능)
//	@GetMapping("/modify/{postSeq}")
//	public String modifyForm(Model model, PostForm postForm, @PathVariable("postSeq") Integer postSeq, Principal principal) {
//	    KnowledgePost post = postService.getPost(postSeq);
//	    
//	    // 1. 로그인 여부 확인 및 2. 권한 체크 (핵심 로직)
//	    if (principal == null) {
//	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 수정이 가능합니다.");
//	    }
//	    if (!post.getUser().getUserEmail().equals(principal.getName())) {
//	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
//	    }
//
//		postForm.setPostTitle(post.getPostTitle());
//		postForm.setPostContent(post.getPostContent());
//		model.addAttribute("postSeq", postSeq);
//		return "catBoard/post_modify";
//	}
//
//	// 게시글 수정 처리 (작성자 본인만 가능)
//	@PostMapping("/modify/{postSeq}")
//	public String modify(@Valid PostForm postForm, BindingResult bindingResult,
//			@PathVariable("postSeq") Integer postSeq, Principal principal) { 
//		
//		if (bindingResult.hasErrors()) {
//			return "catBoard/post_modify";
//		}
//		
//		KnowledgePost post = this.postService.getPost(postSeq);
//		
//        // 1. 로그인 여부 확인
//		if (principal == null) {
//		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 수정이 가능합니다.");
//		}
//        
//		// 2. 권한 체크: 현재 로그인된 사용자(principal.getName() = username)와 작성자 비교
//		if (!post.getUser().getUserEmail().equals(principal.getName())) {
//		    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다. 작성자만 수정이 가능합니다");
//		}
//		
//		this.postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
//		return String.format("redirect:/post/detail/%s" , postSeq);
//	}
//
//
//	// 게시글 삭제 (작성자 본인만 가능)
//	@GetMapping("/delete/{postSeq}")
//	public String postDelete(@PathVariable("postSeq") Integer postSeq, Principal principal) { 
//		KnowledgePost post = postService.getPost(postSeq);
//		
//        // 1. 로그인 여부 확인
//		if (principal == null) {
//		    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 삭제가 가능합니다.");
//		}
//        
//		// 2. 권한 체크: 현재 로그인된 사용자(principal.getName() = username)와 작성자 비교
//		if (!post.getUser().getUserEmail().equals(principal.getName())) {
//		    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다. 작성자만 삭제 가능합니다");
//		}
//		
//		// 게시판 코드(String)를 가져와서 리다이렉트
//		String boardCode = post.getKnowledgeBoard().getBoardCode();
//		postService.delete(post);
//		
//		return "redirect:/post/list/" + boardCode;
//	}
//}



package com.project.fatcat.catBoard.controller;

import java.security.Principal; 
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException; 

import com.project.fatcat.catBoard.form.CommentForm;
import com.project.fatcat.catBoard.form.PostForm;

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

    // 게시판별 글 목록 (페이징 포함)
    @GetMapping("/list/{boardCode}")
    public String list(@PathVariable("boardCode") String boardCode,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {

        // 게시판 이름 매핑
        Map<String, String> boardNames = new HashMap<>();
        boardNames.put("1", "수의사에게 질문하기");
        boardNames.put("2", "냥꿀팁");
        model.addAttribute("boardName", boardNames.get(boardCode));
        model.addAttribute("currentBoardSeq", boardCode);
        model.addAttribute("boardCode", boardCode); // 템플릿 '질문 등록' 버튼용

        // 페이징 설정 (페이지당 10개, 최신순)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<KnowledgePost> paging = postService.getPostsByBoardCode(boardCode, pageable);
        model.addAttribute("paging", paging);

        return "catBoard/post_list";
    }

    // 게시글 상세
    @GetMapping("/detail/{postSeq}")
    public String postDetail(Model model, @PathVariable("postSeq") Integer postSeq, 
                             PostForm postForm, CommentForm commentForm, Principal principal) {

        KnowledgePost post = postService.getPost(postSeq);
        model.addAttribute("post", post);

        postForm.setPostTitle(post.getPostTitle());
        postForm.setPostContent(post.getPostContent());

        return "catBoard/post_detail";
    }

    // 게시글 작성 폼 (로그인 필요)
    @GetMapping("/create/{boardCode}")
    public String createForm(@PathVariable("boardCode") String boardCode,
                             PostForm postForm, Model model, Principal principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "글 작성은 로그인 후 이용 가능합니다.");
        }

        model.addAttribute("boardCode", boardCode);
        return "catBoard/post_form";
    }

    // 게시글 작성 처리
    @PostMapping("/create/{boardCode}")
    public String create(@Valid PostForm postForm, BindingResult bindingResult,
                         @PathVariable("boardCode") String boardCode) {

        if (bindingResult.hasErrors()) {
            return "catBoard/post_form";
        }

        try {
            postService.createPostWithBoardCode(
                postForm.getPostTitle(),
                postForm.getPostContent(),
                boardCode
            );
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return String.format("redirect:/post/list/%s", boardCode);
    }

    // 게시글 수정 폼 (작성자 본인만 가능)
    @GetMapping("/modify/{postSeq}")
    public String modifyForm(Model model, PostForm postForm,
                             @PathVariable("postSeq") Integer postSeq,
                             Principal principal) {

        KnowledgePost post = postService.getPost(postSeq);

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 수정이 가능합니다.");
        }
        if (!post.getUser().getUserEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        postForm.setPostTitle(post.getPostTitle());
        postForm.setPostContent(post.getPostContent());
        model.addAttribute("postSeq", postSeq);

        return "catBoard/post_modify";
    }

    // 게시글 수정 처리 (작성자 본인만 가능)
    @PostMapping("/modify/{postSeq}")
    public String modify(@Valid PostForm postForm, BindingResult bindingResult,
                         @PathVariable("postSeq") Integer postSeq,
                         Principal principal) {

        if (bindingResult.hasErrors()) {
            return "catBoard/post_modify";
        }

        KnowledgePost post = this.postService.getPost(postSeq);

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 수정이 가능합니다.");
        }
        if (!post.getUser().getUserEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다. 작성자만 수정이 가능합니다.");
        }

        this.postService.modify(post, postForm.getPostTitle(), postForm.getPostContent());
        return String.format("redirect:/post/detail/%s", postSeq);
    }

    // 게시글 삭제 (작성자 본인만 가능)
    @GetMapping("/delete/{postSeq}")
    public String postDelete(@PathVariable("postSeq") Integer postSeq,
                             Principal principal) {

        KnowledgePost post = postService.getPost(postSeq);

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인 후 삭제가 가능합니다.");
        }
        if (!post.getUser().getUserEmail().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다. 작성자만 삭제 가능합니다.");
        }

        String boardCode = post.getKnowledgeBoard().getBoardCode();
        postService.delete(post);

        return "redirect:/post/list/" + boardCode;
    }
}
