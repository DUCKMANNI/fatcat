package com.project.fatcat.inquiry.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.fatcat.entity.Inquiry;
import com.project.fatcat.inquiry.service.InquiryCommentService;
import com.project.fatcat.inquiry.service.InquiryService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry/comment") // ⭐ 매핑 경로를 /inquiry/comment로 수정 (InquiryController와 분리)
public class InquiryCommentController {
	
	private final InquiryService inquiryService; 
    private final InquiryCommentService inquiryCommentService; 
    
    private boolean isAdmin(HttpSession session) {
        // 실제 Spring Security 권한 확인 로직으로 대체되어야 합니다.
        // 현재는 개발 편의를 위해 true로 가정
        return true; 
    }

    // 답변 등록 페이지 폼 (관리자만 접근 가능)
    @GetMapping("/create/{inquirySeq}")
    public String showAnswerForm(@PathVariable("inquirySeq") Integer inquirySeq, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/error/403";
        }

        Optional<Inquiry> optionalInquiry = inquiryService.getInquiryById(inquirySeq);
        if (optionalInquiry.isPresent()) {
            model.addAttribute("inquiry", optionalInquiry.get());
            // 이 뷰는 답변 등록을 위한 별도의 폼 페이지일 가능성이 높습니다.
            return "inquiry/answer_form";
        } else {
            return "redirect:/inquiry/list";
        }
    }

    // 답변 등록 처리 (관리자만 가능)
    @PostMapping("/create/{inquirySeq}")
    public String submitAnswer(
            @PathVariable("inquirySeq") Integer inquirySeq, 
            @RequestParam("inquiryComment") String inquiryComment, 
            HttpSession session) {
        
        if (!isAdmin(session)) {
            return "redirect:/error/403";
        }
        
        // ⭐⭐ 추가: 내용이 비어있는지 확인하여 400 에러 방지 ⭐⭐
        if (inquiryComment == null || inquiryComment.trim().isEmpty()) {
            // 빈 답변이면 다시 폼으로 돌아가도록 처리 (오류 메시지와 함께)
            // 현재는 간단히 상세 페이지로 리다이렉트 (실제로는 flash attribute로 에러 메시지 전달 필요)
            System.err.println("ERROR: Comment content cannot be empty.");
            return "redirect:/inquiry/detail/" + inquirySeq; 
        }

        // 서비스 호출
        inquiryCommentService.saveAnswer(inquirySeq, inquiryComment); 
        
        return "redirect:/inquiry/list"; 
    }
}
