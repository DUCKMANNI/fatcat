

package com.project.fatcat.cats.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.fatcat.cats.dto.CatAddDTO;
import com.project.fatcat.cats.dto.CatUpdateDTO;
import com.project.fatcat.cats.service.CatService;
import com.project.fatcat.entity.Cat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cats")
public class CatController {
	
	private final CatService catService;
	
	// 고양이 정보 추가 폼
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("catAddDTO", new CatAddDTO());
        model.addAttribute("breeds", catService.getAllBreeds()); // 종 목록
        return "cats/cat_add";
    }
    
 // 추가 처리
    @PostMapping("/add")
    public String addSubmit(@ModelAttribute("catAddDTO") CatAddDTO dto) {

         catService.addCat(dto);

        return "redirect:/mypage#cat-info";
    }

    // 고양이 정보 수정 폼 페이지를 보여주는 GET 메서드
//    @GetMapping("/modify/{catSeq}")
//    public String showModifyForm(@PathVariable("catSeq") Integer catSeq, Model model) {
//        // 1. 서비스에서 고양이 정보 조회
//        Optional<Cat> catOptional = catService.findById(catSeq);
//
//        // 2. 고양이 정보가 존재하는지 확인
//        if (catOptional.isEmpty()) {
//            // 고양이 정보가 없으면 에러 페이지 또는 리스트 페이지로 리다이렉트
//            return "redirect:/mypage"; // 예시: 마이페이지로 리다이렉트
//        }
//        
//        // 3. Optional에서 Cat 엔티티 추출
//        Cat cat = catOptional.get();
//
//        // 4. Cat 엔티티의 정보를 DTO에 담아 HTML 폼에 전달
//        CatUpdateDTO catUpdateDTO = CatUpdateDTO.builder()
//            .catName(cat.getCatName())
//            .catBirthday(cat.getCatBirtthday())
////            .catImageFile(cat.CatImageFile())
//            .catGender(cat.getCatGender())
//            .catBreed(cat.getCatBreed())
//            .isNeutered(cat.isNeutered())
//            .hasDisease(cat.getHasDisease())
//            .hasAllergy(cat.getHasAllergy())
//            .build();
//        
//        
//        model.addAttribute("cat", cat);
//        model.addAttribute("currentImageUrl", cat.getCatImageUrl());
//        
//        model.addAttribute("catUpdateDTO", catUpdateDTO);
//        model.addAttribute("catSeq", catSeq);
//        
//        // Enum 값을 드롭다운에 전달하기 위해 추가
//        model.addAttribute("genders", Cat.Gender.values());
//
//        // 고양이 종 목록을 모델에 추가
//        model.addAttribute("breeds", catService.getAllBreeds());
//
//        return "cats/cats_modify"; // Thymeleaf 템플릿 경로
//    }

    // 수정된 정보를 받아 처리하는 POST 메서드
    @PostMapping("/modify/{catSeq}")
  public String modifyCat(@Valid @ModelAttribute CatUpdateDTO catUpdateDTO, BindingResult bindingResult, 
                          @PathVariable("catSeq") Integer catSeq, Model model, RedirectAttributes redirectAttributes) {

      if (bindingResult.hasErrors()) {
          // 유효성 검사 실패 시, 다시 폼 페이지로 돌아가기
          return "cats/cats_modify";
      }
      
       this.catService.updateCat(catSeq, catUpdateDTO);
       return "redirect:/mypage";
  }
}


