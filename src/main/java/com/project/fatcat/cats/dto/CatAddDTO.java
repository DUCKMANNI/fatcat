package com.project.fatcat.cats.dto;

import com.project.fatcat.entity.Cat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
public class CatAddDTO {

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String catName;

    
    private LocalDate catBirtthday;

    @NotNull(message = "성별은 필수 선택 항목입니다.")
    private Cat.Gender catGender;

    @NotNull(message = "중성화 여부는 필수 선택 항목입니다.")
    private Boolean isNeutered;

    private String catBreed;

    private String hasDisease;
    private String hasAllergy;
    private String catImageUrl;

}
