package com.project.fatcat.cats.dto;

import java.time.LocalDate;

import com.project.fatcat.entity.Cat;

import groovy.transform.ToString;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CatUpdateDTO {
	
	    @NotEmpty(message = "고양이 이름은 필수입니다.")
	    private String catName;

	    
	    private LocalDate catBirtthday;

	    
	    private String catImageUrl;


	    @NotNull(message = "고양이 성별은 필수입니다.")
	    private Cat.Gender catGender;
	    
	  
	    private String catBreed;
	    
	    // Boolean 타입은 기본값이 false이므로 @NotNull을 사용
	    @NotNull(message = "중성화 여부는 필수입니다.")
	    private Boolean isNeutered;
	    
	    
	    private String hasDisease;
	    
	    
	    private String hasAllergy;
}
