//package com.project.fatcat.cats.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.project.fatcat.cats.dto.CatAddDTO;
//import com.project.fatcat.cats.dto.CatUpdateDTO;
//import com.project.fatcat.cats.service.CatService;
//import com.project.fatcat.entity.Cat;
//
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//
//@Controller
//@RequiredArgsConstructor
//@RequestMapping("/cats")
//public class CatController {
//	
//	private final CatService catService;
//
//    /**
//     * 고양이 정보 수정 폼 페이지를 보여주는 GET 메서드
//     * @param catSeq 수정할 고양이의 고유 번호
//     * @param model 뷰에 전달할 데이터를 담는 객체
//     * @return 고양이 정보 수정 페이지의 뷰 이름
//     */
//    @GetMapping("/modify/{catSeq}")
//    public String showModifyForm(@PathVariable("catSeq") Integer catSeq, Model model) {
//        // 1. 서비스에서 고양이 정보 조회
//        Cat cat = catService.findById(catSeq);
//
//        // 4. Cat 엔티티의 정보를 DTO에 담아 HTML 폼에 전달
//        CatUpdateDTO catUpdateDTO = new CatUpdateDTO();
//        
//        catUpdateDTO.setCatName(cat.getCatName());
//        catUpdateDTO.setCatBirtthday(cat.getCatBirtthday()); // catBirtthday 필드명을 유지합니다.
//        catUpdateDTO.setCatImageUrl(cat.getCatImageUrl());
//        catUpdateDTO.setCatGender(cat.getCatGender());
//        catUpdateDTO.setCatBreed(cat.getCatBreed());
//        catUpdateDTO.setIsNeutered(cat.isNeutered());
//        catUpdateDTO.setHasDisease(cat.getHasDisease());
//        catUpdateDTO.setHasAllergy(cat.getHasAllergy());
//        
//        model.addAttribute("catUpdateDTO", catUpdateDTO);
//        model.addAttribute("catSeq", catSeq);
//        model.addAttribute("cat", cat);
//        
//        // Enum 값을 드롭다운에 전달하기 위해 추가
//        model.addAttribute("genders", Cat.Gender.values());
//
//        return "cats/cats_modify";
//    }
//
//    /**
//     * 수정된 정보를 받아 처리하는 POST 메서드
//     * @param catUpdateDTO 폼에서 제출된 데이터
//     * @param bindingResult 유효성 검사 결과
//     * @param catSeq 수정할 고양이의 고유 번호
//     * @param model 뷰에 전달할 데이터를 담는 객체
//     * @return 리다이렉트할 페이지 경로
//     */
//    @PostMapping("/modify/{catSeq}")
//    public String modifyCat(@Valid @ModelAttribute CatUpdateDTO catUpdateDTO, BindingResult bindingResult, 
//                            @PathVariable("catSeq") Integer catSeq, Model model, RedirectAttributes redirectAttributes) {
//
//        if (bindingResult.hasErrors()) {
//            // 유효성 검사 실패 시, 다시 폼 페이지로 돌아가기
//            return "cats/cats_modify";
//        }
//        
//         this.catService.updateCat(catSeq, catUpdateDTO);
//         return "redirect:/mypage";
//    }
//
//    // --- 고양이 추가 기능 ---
//
//    /**
//     * 고양이 추가 폼 페이지를 보여주는 GET 메서드
//     * @param model 뷰에 전달할 데이터를 담는 객체
//     * @return 고양이 추가 페이지의 뷰 이름
//     */
//    @GetMapping("/add")
//    public String showAddForm(Model model) {
//        // 폼을 위한 빈 DTO 객체를 모델에 추가
////        model.addAttribute("catAddDTO", new CatAddDTO());
//        // 성별 선택을 위한 Enum 값을 모델에 추가
//        model.addAttribute("genders", Cat.Gender.values());
//        return "cats/catAdd";
//    }
//
//    /**
//     * 고양이 정보를 받아 처리하는 POST 메서드
//     * @param catAddDTO 폼에서 제출된 데이터
//     * @param bindingResult 유효성 검사 결과
//     * @param model 뷰에 전달할 데이터를 담는 객체
//     * @return 리다이렉트할 페이지 경로
//     */
//    @PostMapping("/add")
//    public String addCat(@Valid @ModelAttribute CatAddDTO catAddDTO, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            // 유효성 검사 실패 시, 다시 폼 페이지로 돌아가기
//            model.addAttribute("genders", Cat.Gender.values());
//            return "cats/catAdd";
//        }
//
//        // 서비스의 비즈니스 로직 호출
//        catService.addCat(catAddDTO);
//
//        // 추가 완료 후 고양이 리스트 페이지로 리다이렉트
//        return "redirect:/mypage";
//    }
//}





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

    // 고양이 정보 수정 폼 페이지를 보여주는 GET 메서드
    @GetMapping("/modify/{catSeq}")
    public String showModifyForm(@PathVariable("catSeq") Integer catSeq, Model model) {
        // 1. 서비스에서 고양이 정보 조회
        Optional<Cat> catOptional = catService.findById(catSeq);

        // 2. 고양이 정보가 존재하는지 확인
        if (catOptional.isEmpty()) {
            // 고양이 정보가 없으면 에러 페이지 또는 리스트 페이지로 리다이렉트
            return "redirect:/mypage"; // 예시: 마이페이지로 리다이렉트
        }
        
        // 3. Optional에서 Cat 엔티티 추출
        Cat cat = catOptional.get();

        // 4. Cat 엔티티의 정보를 DTO에 담아 HTML 폼에 전달
        CatUpdateDTO catUpdateDTO = CatUpdateDTO.builder()
            .catName(cat.getCatName())
            .catBirtthday(cat.getCatBirtthday())
//            .catImageFile(cat.CatImageFile())
            .catGender(cat.getCatGender())
            .catBreed(cat.getCatBreed())
            .isNeutered(cat.isNeutered())
            .hasDisease(cat.getHasDisease())
            .hasAllergy(cat.getHasAllergy())
            .build();
        
        
        model.addAttribute("cat", cat);
        model.addAttribute("currentImageUrl", cat.getCatImageUrl());
        
        model.addAttribute("catUpdateDTO", catUpdateDTO);
        model.addAttribute("catSeq", catSeq);
        
        // Enum 값을 드롭다운에 전달하기 위해 추가
        model.addAttribute("genders", Cat.Gender.values());

        // 고양이 종 목록을 모델에 추가
        model.addAttribute("breeds", List.of(
            "코리안 숏헤어", "페르시안", "샴", "러시안 블루", 
            "아메리칸 숏헤어", "스코티시 폴드", "벵갈", "노르웨이 숲 고양이",
            "메인 쿤", "아비시니안", "터키시 앙고라", "스핑크스",
            "렉돌", "브리티시 숏헤어", "먼치킨", "시베리안",
            "뱅갈", "데본 렉스", "아메리칸 컬"
        ));

        return "cats/cats_modify"; // Thymeleaf 템플릿 경로
    }

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


