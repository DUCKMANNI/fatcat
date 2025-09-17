package com.project.fatcat.care.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.fatcat.care.dto.CareServiceBoardDto;
import com.project.fatcat.care.service.CareServiceBoardService;
import com.project.fatcat.entity.CareServiceBoard;

import jakarta.validation.Valid; 

@Controller
@RequestMapping("/care")
public class CareServiceBoardController {

    @Autowired
    private CareServiceBoardService careServiceBoardService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("careServiceBoardDto", new CareServiceBoardDto());
        return "care/care_form";
    }

    @PostMapping("/create")
    public String createCareServiceBoard(@Valid @ModelAttribute CareServiceBoardDto careServiceBoardDto, BindingResult bindingResult) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            return "care/care_form"; // 오류가 있으면 폼 페이지로 돌아감
        }
        
        // 유효성 검사 통과 시
        careServiceBoardService.save(careServiceBoardDto);
        return "redirecrt:/care/list";
    }
    
    @GetMapping("/list")
    @ResponseBody
    public ResponseEntity<List<CareServiceBoard>> getCareBoardList(@RequestParam(required = false) String region) {
        List<CareServiceBoard> boards;
        if (region != null && !region.isEmpty()) {
            boards = careServiceBoardService.getBoardsByRegion(region);
        } else {
            boards = careServiceBoardService.getAllBoards();
        }
        return ResponseEntity.ok(boards);
    }
    
    @GetMapping("/nearby")
    @ResponseBody
    public ResponseEntity<List<CareServiceBoard>> getNearbyBoards(@RequestParam Double latitude, @RequestParam Double longitude) {
        // 검색 반경 (예: 5km)
        double radiusInKm = 5.0; 
        List<CareServiceBoard> boards = careServiceBoardService.getBoardsWithinRadius(latitude, longitude, radiusInKm);
        return ResponseEntity.ok(boards);
    }
}