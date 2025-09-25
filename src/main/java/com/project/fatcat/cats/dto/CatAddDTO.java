package com.project.fatcat.cats.dto;

import com.project.fatcat.entity.Cat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class CatAddDTO {

//    @NotBlank(message = "이름은 필수 입력 항목입니다.")
//    private String catName;
//
//    
//    private LocalDate catBirtthday;
//
//    @NotNull(message = "성별은 필수 선택 항목입니다.")
//    private Cat.Gender catGender;
//
//    @NotNull(message = "중성화 여부는 필수 선택 항목입니다.")
//    private Boolean isNeutered;
//
//    private String catBreed;
//
//    private String hasDisease;
//    private String hasAllergy;
//    private String catImageUrl;
	
	
	@NotEmpty(message="고양이 이름은 필수입니다.")
    private String catName;

    private LocalDate catBirtthday;

    private String catImageUrl;
    private MultipartFile catImageFile;
    
    @NotNull(message="성별은 필수입니다.")
    private Cat.Gender catGender;

    private String catBreed;

    @NotNull(message="중성화 여부는 필수입니다.")
    private Boolean isNeutered;

    // 질병은 여러 개 선택 가능하도록 List<String> 타입으로 변경
    private String hasDisease;

    private String hasAllergy;
    
    
    

}
