package com.project.fatcat.care.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.access.prepost.PreAuthorize; // 💡 추가: @PreAuthorize 사용

import com.project.fatcat.SecurityUtils;
import com.project.fatcat.care.dto.CareServiceBoardDto;
import com.project.fatcat.care.dto.CareServiceBoardListDto;
import com.project.fatcat.care.service.CareServiceBoardService;
import com.project.fatcat.entity.CareServiceBoard;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/care")
public class CareServiceBoardController {
	
	@GetMapping 
	public String careHome(Model model) { 
        try {
            // SecurityUtils를 사용하여 현재 로그인된 userSeq (Integer 타입)를 가져옵니다.
            Integer loggedInUserSeq = SecurityUtils.getCurrentUserSeq();
            
            // 로그인 상태인 경우 userSeq를 모델에 추가합니다. (HTML에서 loggedInUserSeq를 사용)
            model.addAttribute("loggedInUserSeq", loggedInUserSeq); 
        } catch (IllegalStateException e) {
            // 비로그인 상태인 경우, null을 전달합니다.
            model.addAttribute("loggedInUserSeq", null); 
        }
        
		return "care/care_map"; 
	}

    @Autowired
    private CareServiceBoardService careServiceBoardService;

    // 💡 1차 보안: 로그인된 사용자만 접근 가능
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String showCreateForm(Model model) {
    	
    		CareServiceBoardDto careServiceBoardDto = new CareServiceBoardDto();
    	
    	    // SecurityUtils를 사용하여 현재 로그인된 userSeq를 DTO에 설정합니다.
            careServiceBoardDto.setUserSeq(SecurityUtils.getCurrentUserSeq());
    		
        model.addAttribute("careServiceBoardDto", careServiceBoardDto);
        return "care/care_form";
    }

    // 💡 1차 보안: 로그인된 사용자만 접근 가능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createCareServiceBoard(@Valid @ModelAttribute CareServiceBoardDto careServiceBoardDto, BindingResult bindingResult) {
    	
    			if (bindingResult.hasErrors()) {
             // 유효성 검사 실패 시 userSeq를 다시 설정하여 hidden 필드를 유지
             careServiceBoardDto.setUserSeq(SecurityUtils.getCurrentUserSeq());
             return "care/care_form";
         }
         
         // SecurityUtils를 사용하여 로그인 사용자 userSeq를 DTO에 세팅 (2차 유효성)
         Integer userSeq = SecurityUtils.getCurrentUserSeq();
         careServiceBoardDto.setUserSeq(userSeq);
         
        careServiceBoardService.save(careServiceBoardDto);
        return "redirect:/care";
    }
    
    // ... (getCareListJson, getNearbyBoards 생략 - 인증 불필요)
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<CareServiceBoardListDto>> getCareListJson(
        @RequestParam(name = "region", required = false) String region) {

        List<CareServiceBoardListDto> dtoList;
        if (region != null && !region.isEmpty()) {
            dtoList = careServiceBoardService.getBoardsByRegionAsDto(region);
        } else {
            dtoList = careServiceBoardService.getAllBoardsAsDto();
        }
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/nearby")
    @ResponseBody
    public ResponseEntity<List<CareServiceBoardListDto>> getNearbyBoards(
        @RequestParam(name = "latitude") Double latitude,
        @RequestParam(name = "longitude") Double longitude) {

        double radiusInKm = 3.0;
        List<CareServiceBoardListDto> dtoList = careServiceBoardService.getBoardsWithinRadiusAsDto(latitude, longitude, radiusInKm);
        return ResponseEntity.ok(dtoList);
    }
    
    // 💡 1차 보안: 로그인된 사용자만 접근 가능
    // 🚨 중요: 여기서 게시글 작성자와 로그인 사용자가 동일한지 확인하는 로직이 필요합니다.
    // 이는 Service 계층에서 수행되어야 합니다.
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{careSeq}")
    public String modifyCareForm(@PathVariable("careSeq") Integer careSeq, Model model) {
        
        // 1. 게시글 조회
        CareServiceBoard careBoard = this.careServiceBoardService.getBoard(careSeq);
        
        // 2. 🚨 컨트롤러에서 권한 체크: SecurityUtils를 이용해 로그인 사용자 ID 획득
        Integer loggedInUserSeq = SecurityUtils.getCurrentUserSeq();

        // 3. 🚨 권한이 없으면 접근 차단 (비인가된 접근에 대한 서버 측 방어)
        if (!careBoard.getUser().getUserSeq().equals(loggedInUserSeq)) {
            // 일반적으로 HttpStatus.FORBIDDEN(403) 오류 페이지로 리다이렉트하거나 예외를 던집니다.
            // 여기서는 단순 리다이렉트로 처리합니다.
            return "redirect:/care";
        }
        
        // DTO 매핑
        CareServiceBoardDto careServiceBoardDto = new CareServiceBoardDto();
        careServiceBoardDto.setUserSeq(careBoard.getUser().getUserSeq());
        careServiceBoardDto.setCareTitle(careBoard.getCareTitle());
        careServiceBoardDto.setCareContent(careBoard.getCareContent());
        careServiceBoardDto.setAddress1(careBoard.getAddress1());
        careServiceBoardDto.setAddress2(careBoard.getAddress2());
        careServiceBoardDto.setLatitude(careBoard.getLatitude());
        careServiceBoardDto.setLongitude(careBoard.getLongitude());
        careServiceBoardDto.setPrice(careBoard.getPrice());
       
        model.addAttribute("careSeq", careSeq); 
        model.addAttribute("careServiceBoardDto", careServiceBoardDto);
        return "care/care_form"; // 수정 폼으로 이동
    }

    // 💡 1차 보안: 로그인된 사용자만 접근 가능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{careSeq}")
    public String modifyCare(@PathVariable("careSeq") Integer careSeq, 
                             @Valid @ModelAttribute CareServiceBoardDto careServiceBoardDto, 
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "care/care_form";
        }
        
        // SecurityUtils를 사용하여 userSeq를 가져옵니다. (작성자 검증용)
        Integer userSeq = SecurityUtils.getCurrentUserSeq();
        
        // 2차 보안: 서비스 계층에서 수정 권한(작성자) 로직 처리
        careServiceBoardService.modify(careSeq, careServiceBoardDto, userSeq);
        return "redirect:/care";
    }

    // 💡 1차 보안: 로그인된 사용자만 접근 가능
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{careSeq}")
    public String deleteCare(@PathVariable("careSeq") Integer careSeq) {
        
    	// SecurityUtils를 사용하여 userSeq를 가져옵니다. (작성자 검증용)
    	Integer userSeq = SecurityUtils.getCurrentUserSeq();
    	
        // 2차 보안: 서비스 계층에서 삭제 권한(작성자) 로직 처리
        this.careServiceBoardService.deleteBoard(careSeq, userSeq);
        return "redirect:/care";
    }
}