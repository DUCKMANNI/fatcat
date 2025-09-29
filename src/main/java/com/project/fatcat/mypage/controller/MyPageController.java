package com.project.fatcat.mypage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.cats.service.CatService;
import com.project.fatcat.entity.Cat;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {
    

    private final CatService catService;
    private final UserRepository userRepository; // UserRepository DI

    // 마이페이지 URL로 접속하면 이 메소드가 호출됩니다.
    @GetMapping
    public String myPage(Model model) {
        
        int userSeq = 1; 

        
        List<Cat> cats = catService.findAllByUserId(userSeq);

        
        User user = userRepository.findById(userSeq)
                                  .orElseThrow(() -> new RuntimeException("User not found"));

       
        model.addAttribute("cats", cats);
        model.addAttribute("user", user);
        
       
        return "mypage/myPage";
    }
}