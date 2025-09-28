package com.project.fatcat.faqs.controller;

import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.fatcat.entity.Faq;
import com.project.fatcat.faqs.service.FaqService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Controller
@RequiredArgsConstructor
@RequestMapping("/faq")
public class FaqController {
	
	private final FaqService faqService;
	
//	 // 가정: 세션에서 사용자 역할을 확인하는 예시 메서드
//    private boolean isAdmin(HttpSession session) {
//        // 실제 애플리케이션에서는 로그인된 사용자의 ROLE을 확인하는 로직으로 교체해야 합니다.
//        // 예: User user = (User) session.getAttribute("loggedInUser");
//        //     return user != null && "ADMIN".equals(user.getRole());
//        return true; // 테스트를 위해 임시로 true 반환
//    }
//
// // FAQ 목록 페이지 (페이징 적용)
//    @GetMapping("/list")
//    public String list(Model model, HttpSession session, @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable) {
//        Page<Faq> paging = faqService.getAllFaqs(pageable);
//        model.addAttribute("paging", paging);
//        model.addAttribute("isAdmin", isAdmin(session));
//        return "faq/faq_list";
//    }
//
//    // 질문 등록 페이지 폼
//    @GetMapping("/ask")
//    public String showAskForm() {
//        return "faq/ask_form";
//    }
//
//    // 질문 등록 처리 (누구나 가능)
//    @PostMapping("/ask")
//    public String submitQuestion(@RequestParam("question") String question) {
//        faqService.createQuestion(question);
//        return "redirect:/faq/list";
//    }
//
//    // 답변 등록 페이지 폼 (관리자만 접근 가능)
//    @GetMapping("/answer/{faqSeq}")
//    public String showAnswerForm(@PathVariable("faqSeq") Integer faqSeq, Model model, HttpSession session) {
//        if (!isAdmin(session)) {
//            return "redirect:/error/403";
//        }
//        
//        Optional<Faq> optionalFaq = faqService.getFaqById(faqSeq);
//        if (optionalFaq.isPresent()) {
//            model.addAttribute("faq", optionalFaq.get());
//            return "faq/answer_form";
//        } else {
//            return "redirect:/faq/list";
//        }
//    }
//
//    // 답변 등록 처리 (관리자만 가능)
//    @PostMapping("/answer/{faqSeq}")
//    public String submitAnswer(@PathVariable("faqSeq") Integer faqSeq, @RequestParam("answer") String answer, HttpSession session) {
//        if (!isAdmin(session)) {
//            return "redirect:/error/403";
//        }
//        
//        faqService.saveAnswer(faqSeq, answer);
//        return "redirect:/faq/list";
//    }
	
	
	 // 가정: 세션에서 사용자 역할을 확인하는 예시 메서드
    private boolean isAdmin(HttpSession session) {
        // 실제 애플리케이션에서는 로그인된 사용자의 ROLE을 확인하는 로직으로 교체해야 합니다.
        // 예: User user = (User) session.getAttribute("loggedInUser");
        //     return user != null && "ADMIN".equals(user.getRole());
        return true; // 테스트를 위해 임시로 true 반환
    }

    // FAQ 목록 페이지 (페이징 및 검색 적용)
    @GetMapping("/list")
    public String list(
            Model model, 
            HttpSession session, 
            @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<Faq> paging = faqService.getAllFaqs(pageable, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("isAdmin", isAdmin(session));
        model.addAttribute("keyword", keyword); // 검색 상태 유지를 위해 keyword를 모델에 추가
        return "faq/faq_list";
    }

    // 질문 등록 페이지 폼 (누구나 접근 가능)
    @GetMapping("/ask")
    public String showAskForm() {
        return "faq/ask_form";
    }

    // 질문 등록 처리 (누구나 가능)
    @PostMapping("/ask")
    public String submitQuestion(@RequestParam("question") String question) {
        faqService.createQuestion(question);
        return "redirect:/faq/list";
    }

    // 답변 등록 페이지 폼 (관리자만 접근 가능)
    @GetMapping("/answer/{faqSeq}")
    public String showAnswerForm(@PathVariable("faqSeq") Integer faqSeq, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/error/403";
        }

        Optional<Faq> optionalFaq = faqService.getFaqById(faqSeq);
        if (optionalFaq.isPresent()) {
            model.addAttribute("faq", optionalFaq.get());
            return "redirect:faq/list";
        } else {
            return "redirect:/faq/list";
        }
    }

    // 답변 등록 처리 (관리자만 가능)
    @PostMapping("/answer/{faqSeq}")
    public String submitAnswer(@PathVariable("faqSeq") Integer faqSeq, @RequestParam("answer") String answer, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/error/403";
        }

        faqService.saveAnswer(faqSeq, answer);
        return "redirect:/faq/list";
    }

}
