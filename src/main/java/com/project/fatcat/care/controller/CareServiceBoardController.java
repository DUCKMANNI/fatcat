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

import com.project.fatcat.care.dto.CareServiceBoardDto;
import com.project.fatcat.care.dto.CareServiceBoardListDto;
import com.project.fatcat.care.service.CareServiceBoardService;
import com.project.fatcat.entity.CareServiceBoard;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/care")
public class CareServiceBoardController {
	
	@GetMapping
	public String careHome() {
		return "care/care_map";
	}

    @Autowired
    private CareServiceBoardService careServiceBoardService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("careServiceBoardDto", new CareServiceBoardDto());
        return "care/care_form";
    }

    @PostMapping("/create")
    public String createCareServiceBoard(@Valid @ModelAttribute CareServiceBoardDto careServiceBoardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "care/care_form";
        }
        careServiceBoardService.save(careServiceBoardDto);
        return "redirect:/care";
    }

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
    
    // 수정 페이지를 보여주는 GET 메서드
    @GetMapping("/modify/{careSeq}")
    public String modifyCareForm(@PathVariable("careSeq") Integer careSeq, Model model) {
        // 기존 게시글 정보를 가져와서 DTO에 담아 뷰에 전달
        CareServiceBoard careBoard = this.careServiceBoardService.getBoard(careSeq);
        
        CareServiceBoardDto careServiceBoardDto = new CareServiceBoardDto();
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

    // 수정된 내용을 처리하는 POST 메서드
    @PostMapping("/modify/{careSeq}")
    public String modifyCare(@PathVariable("careSeq") Integer careSeq, 
                             @Valid @ModelAttribute CareServiceBoardDto careServiceBoardDto, 
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "care/care_form";
        }
        // 서비스 계층에서 수정 로직 처리
        careServiceBoardService.modify(careSeq, careServiceBoardDto);
        return "redirect:/care";
    }

    // 삭제를 처리하는 POST 메서드
    @PostMapping("/delete/{careSeq}")
    public String deleteCare(@PathVariable("careSeq") Integer careSeq) {
        // 서비스 계층에서 삭제 로직 처리
        this.careServiceBoardService.deleteBoard(careSeq); // ID를 직접 넘겨주는 것이 더 효율적
        return "redirect:/care";
    }
}