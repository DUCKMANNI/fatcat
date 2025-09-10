package com.project.fatcat.shopping.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryDTO {

	private String mainCategory;
    private String mainCode;
    private List<SubCategoryDTO> subCategories;

    @Getter @Setter @AllArgsConstructor
    public static class SubCategoryDTO {
        private String subCategory;
        private String subCode;
        private List<DetailCategoryDTO> details;
    }

    @Getter @Setter @AllArgsConstructor
    public static class DetailCategoryDTO {
        private String detailCategory;
    }
	
}
