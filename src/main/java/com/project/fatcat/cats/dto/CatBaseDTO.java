package com.project.fatcat.cats.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.entity.Cat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CatBaseDTO {

	@NotEmpty(message = "고양이 이름은 필수입니다.")
	private String catName;

	private LocalDate catBirtthday;

	private String catImageUrl;
	private MultipartFile catImageFile;

	@NotNull(message = "성별은 필수입니다.")
	private Cat.Gender catGender;

	private String catBreed; // 종

	@NotNull(message = "중성화 여부는 필수입니다.")
	private Boolean isNeutered;

	private String hasDisease;

	private String hasAllergy;
}
