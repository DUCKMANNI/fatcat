package com.project.fatcat.inquiry.controller;

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

import com.project.fatcat.entity.Inquiry;
import com.project.fatcat.inquiry.form.InquiryForm;
import com.project.fatcat.inquiry.service.InquiryService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/inquiry") // ⭐ 매핑 경로를 /inquiry로 변경했습니다.
public class InquiryController {
	
	private final InquiryService inquiryService;

	 // 가정: 세션에서 사용자 역할을 확인하는 예시 메서드
   private boolean isAdmin(HttpSession session) {
       // 실제 Security Context를 확인하는 로직으로 교체해야 합니다.
       return true; 
   }

   // (기존 코드와 동일) 문의 목록 페이지
   @GetMapping("/list")
   public String list(
           Model model, 
           HttpSession session, 
           @PageableDefault(size = 10, sort = "createDate", direction = Sort.Direction.DESC) Pageable pageable,
           @RequestParam(value = "keyword", required = false) String keyword
   ) {
       Page<Inquiry> paging = inquiryService.getAllInquiries(pageable, keyword);
       model.addAttribute("paging", paging);
       model.addAttribute("isAdmin", isAdmin(session));
       model.addAttribute("keyword", keyword);
       return "inquiry/inquiry_list";
   }

   // ⭐ 수정: 질문 등록 페이지 폼 (DTO 객체를 모델에 추가)
   @GetMapping("/ask")
   public String showAskForm(Model model) {
       model.addAttribute("inquiryForm", new InquiryForm()); // 폼 객체를 모델에 담아 전달
       return "inquiry/inquiry_ask"; // ⭐ 템플릿 이름을 inquiry_ask.html로 가정
   }

   // ⭐ 수정: 질문 등록 처리 (InquiryForm DTO를 받아 Service 호출)
   @PostMapping("/ask")
   public String submitQuestion(InquiryForm inquiryForm) {
       // InquiryForm DTO를 서비스로 전달
       inquiryService.createQuestion(inquiryForm);
       return "redirect:/inquiry/list";
   }

   // (기존 코드와 동일) 문의 상세 페이지
   @GetMapping("/{inquirySeq}")
   public String viewDetail(@PathVariable("inquirySeq") Integer inquirySeq, Model model, HttpSession session) {
       Optional<Inquiry> optionalInquiry = inquiryService.getInquiryById(inquirySeq);
       
       if (optionalInquiry.isPresent()) {
           model.addAttribute("inquiry", optionalInquiry.get());
           model.addAttribute("isAdmin", isAdmin(session));
           return "inquiry/inquiry_detail"; 
       } else {
           return "redirect:/inquiry/list";
       }
   } 


}
