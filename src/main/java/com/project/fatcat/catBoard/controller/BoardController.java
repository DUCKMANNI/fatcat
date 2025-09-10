package com.project.fatcat.catBoard.controller;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

import ch.qos.logback.core.net.SyslogOutputStream;
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
    	
    	
        if(boardCode == null) boardCode = "main"; // 기본 코드
        
        List<KnowledgeBoard> boards = boardService.getBoard();
       
        System.out.println(boards.toString()); // 시뮬레이션 확인
        
        model.addAttribute("boards", boards);

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createDate").descending());
//        Page<KnowledgePost> posts = postService.getPostsByBoardCode(boardCode, page);
//        model.addAttribute("posts", posts);
        return "board_list";
        
    }
    
 
    
}
