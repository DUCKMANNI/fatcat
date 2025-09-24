package com.project.fatcat.cats.dto;

import java.time.LocalDate;

import com.project.fatcat.entity.Cat.Gender;

import jakarta.validation.constraints.NotEmpty;
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
public class CatUpdateDTO {
	
	    @NotEmpty(message = "고양이 이름은 필수입니다.")
	    private String catName;

	    
	    private LocalDate catBirtthday;

	    
	    private String catImageUrl;


	    @NotEmpty(message = "고양이 성별은 필수입니다.")
	    private Gender catGender;
	    
	   
	    private String catBreed;
	    
	    // Boolean 타입은 기본값이 false이므로 @NotNull을 사용
	    @NotEmpty(message = "중성화 여부는 필수입니다.")
	    private Boolean isNeutered;
	    
	    @NotEmpty(message = "질병 여부는 필수입니다.")
	    private Boolean hasDisease;
	    
	    @NotEmpty(message = "알레르기 여부는 필수입니다.")
	    private Boolean hasAllergy;
}
