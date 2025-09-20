package com.project.fatcat.cats.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.fatcat.cats.dto.CatUpdateDTO;
import com.project.fatcat.cats.repository.CatRepository;
import com.project.fatcat.entity.Cat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cats")
public class CatController {
	
	private final CatRepository catRepository;

	
	
	 // 고양이 정보 수정 폼 페이지를 보여주는 GET 메서드
    @GetMapping("/modify/{catSeq}")
    public String modifyCatForm(@PathVariable("catSeq") Integer catSeq, Model model) {
        Cat cat = catRepository.findById(catSeq).orElseThrow(() -> new IllegalArgumentException("해당 고양이가 없습니다."));

        // Cat 엔티티의 정보를 DTO에 담아 HTML 폼에 전달
        CatUpdateDTO catUpdateDTO = new CatUpdateDTO();
        catUpdateDTO.setCatName(cat.getCatName());
        catUpdateDTO.setCatBirtthday(cat.getCatBirthday());
        catUpdateDTO.setCatImageUrl(cat.getCatImageUrl());
//        catUpdateDTO.setCatGender(cat.getCatGender());
//        catUpdateDTO.setIsNeutered(cat.getIsNeutered());
//        catUpdateDTO.setCatBreed(cat.getCatBreed());
//        catUpdateDTO.setChronicDisease(cat.getChronicDisease());
//        catUpdateDTO.setAllergy(cat.getAllergy());

        model.addAttribute("catUpdateDTO", catUpdateDTO);
        model.addAttribute("catSeq", catSeq);

        return "cat_modify";
    }

    // 수정된 정보를 받아 처리하는 POST 메서드
    @PostMapping("/modify/{catSeq}")
    public String modifyCat(@Valid CatUpdateDTO catUpdateDTO, BindingResult bindingResult, @PathVariable("catSeq") Integer catSeq) {

        if (bindingResult.hasErrors()) {
            return "cat_modify";
        }

        Cat cat = catRepository.findById(catSeq).orElseThrow(() -> new IllegalArgumentException("해당 고양이가 없습니다."));

        // DTO의 정보로 기존 엔티티 업데이트
        cat.setCatName(catUpdateDTO.getCatName());
        cat.setCatBirthday(catUpdateDTO.getCatBirtthday());
        cat.setCatImageUrl(catUpdateDTO.getCatImageUrl());
//        cat.setCatGender(catUpdateDTO.getCatGender());
//        cat.setIsNeutered(catUpdateDTO.getIsNeutered());
//        cat.setCatBreed(catUpdateDTO.getCatBreed());
//        cat.setChronicDisease(catUpdateDTO.getChronicDisease());
//        cat.setAllergy(catUpdateDTO.getAllergy());

        catRepository.save(cat);

        return "redirect:/cats/detail/" + catSeq;
    }
	

}
