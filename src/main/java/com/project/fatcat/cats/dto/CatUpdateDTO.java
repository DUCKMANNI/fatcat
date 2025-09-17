package com.project.fatcat.cats.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CatUpdateDTO {
	
	    @NotEmpty(message = "고양이 이름은 필수입니다.")
	    private String catName;

	    
	    private LocalDate catBirtthday;

	    
	    private String catImageUrl;

//	    @NotEmpty(message = "성별은 필수입니다.")
//	    private String catGender;
//
//	    @NotEmpty(message = "중성화 여부는 필수입니다.")
//	    private Boolean isNeutered;
//
//	    @NotEmpty(message = "고양이 종은 필수입니다.")
//	    private String catBreed;
//
//	    private String chronicDisease;
//
//	    private String allergy;
}
