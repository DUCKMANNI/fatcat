package com.project.fatcat.cats.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.cats.dto.CatUpdateDTO;
import com.project.fatcat.cats.service.CatService;
import com.project.fatcat.entity.Cat;

import jakarta.transaction.Transactional;
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
            .catImageUrl(cat.getCatImageUrl())
            .catGender(cat.getCatGender())
            .catBreed(cat.getCatBreed())
            .isNeutered(cat.isNeutered())
            .hasDisease(cat.isHasDisease())
            .hasAllergy(cat.isHasAllergy())
            .build();
        
        model.addAttribute("catUpdateDTO", catUpdateDTO);
        model.addAttribute("catSeq", catSeq);
        model.addAttribute("cat", cat);
        
        // Enum 값을 드롭다운에 전달하기 위해 추가
        model.addAttribute("genders", Cat.Gender.values());

        return "cats/cats_modify"; // Thymeleaf 템플릿 경로
    }

    // 수정된 정보를 받아 처리하는 POST 메서드
    @PostMapping("/modify/{catSeq}")
    public String modifyCat(@Valid CatUpdateDTO catUpdateDTO, BindingResult bindingResult, 
                            @PathVariable("catSeq") Integer catSeq, Model model) {

        if (bindingResult.hasErrors()) {
            // 유효성 검사 실패 시, 다시 폼 페이지로 돌아가기
            // 이 때, 성별 옵션 데이터를 다시 전달해줘야 함
            model.addAttribute("genders", Cat.Gender.values());
            return "cats/cats_modify";
        }
        
        // 서비스의 비즈니스 로직 호출
		/* catService.updateCat(catSeq, catUpdateDTO); */

        // 수정 완료 후 상세 페이지로 리다이렉트 (예시)
        return "redirect:/cats/detail/" + catSeq;
    }

    
    
    
    
    
    
    /**
     * 고양이 정보를 업데이트하는 메소드
     * @param catSeq 업데이트할 고양이의 고유 번호
     * @param catUpdateDTO 업데이트할 정보가 담긴 DTO
     */
    @Transactional // 트랜잭션 처리를 통해 데이터 변경 작업을 안전하게 실행
    public void updateCat(Integer catSeq, CatUpdateDTO catUpdateDTO) {
        // 1. catSeq를 사용하여 기존 Cat 엔티티를 찾습니다.
        //    만약 찾지 못하면 NoSuchElementException을 발생시킵니다.
        Cat cat = catService.findById(catSeq)
            .orElseThrow(() -> new IllegalArgumentException("Invalid catSeq:" + catSeq));

        // 2. DTO의 정보로 엔티티의 필드를 업데이트합니다.
        cat.setCatName(catUpdateDTO.getCatName());
        cat.setCatBirtthday(catUpdateDTO.getCatBirtthday());
        cat.setCatImageUrl(catUpdateDTO.getCatImageUrl());
        cat.setCatGender(catUpdateDTO.getCatGender());
        cat.setCatBreed(catUpdateDTO.getCatBreed());
        cat.setNeutered(catUpdateDTO.getIsNeutered());
        cat.setHasDisease(catUpdateDTO.getHasDisease());
        cat.setHasAllergy(catUpdateDTO.getHasAllergy());
        
        // 3. 별도의 save() 호출 없이 트랜잭션이 종료되면 변경사항이 자동으로 DB에 반영됩니다.
    }
    
}
