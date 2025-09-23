package com.project.fatcat.catBoard.controller;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



import com.mysql.cj.log.Log;

import com.project.fatcat.catBoard.service.BoardService;
import com.project.fatcat.catBoard.service.PostService;
import com.project.fatcat.entity.KnowledgeBoard;
import com.project.fatcat.entity.KnowledgePost;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final PostService postService;
    


    @GetMapping("/list")
    public String list(
            @RequestParam(name = "boardCode", required = false) String boardCode,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        if (boardCode == null) boardCode = "1"; // 기본 코드

        // 전체 게시판 가져오기
        List<KnowledgeBoard> boards = boardService.getBoard();
        model.addAttribute("boards", boards);

        // 게시판 이름 설정
        String boardName;
        switch (boardCode) {
            case "1":
                boardName = "수의사에게 질문하기";
                break;
            case "2":
                boardName = "냥꿀팁";
                break;
            default:
                boardName = "메인 게시판";
        }
        model.addAttribute("currentBoardName", boardName); // ✨ 템플릿 변수명과 맞춤

        // 게시물 페이징
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
        Page<KnowledgePost> posts = postService.getPostsByBoardCode(boardCode, pageable);
        model.addAttribute("paging", posts); // ✨ 템플릿 변수명과 맞춤

        return "catBoard/post_list"; // 템플릿 이름
    }
    
 
    
}
