package com.project.fatcat.shopping.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.fatcat.entity.Category;
import com.project.fatcat.shopping.dto.CategoryDTO;
import com.project.fatcat.shopping.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryDTO> getCategoryMenu() {

		List<Category> categories = categoryRepository.findAllByOrderByMainCodeAscSubCodeAsc();

		// 대분류 그룹핑
		Map<String, List<Category>> mainCategories = categories.stream()
				.collect(Collectors.groupingBy(Category::getMainCode, LinkedHashMap::new, Collectors.toList()));

		return mainCategories.entrySet().stream().map(mainEntry -> {
			String mainCode = mainEntry.getKey();
			String mainCategory = mainEntry.getValue().get(0).getMainCategory();

			// 중분류 그룹핑
			Map<String, List<Category>> subCategories = mainEntry.getValue().stream()
					.collect(Collectors.groupingBy(Category::getSubCode, LinkedHashMap::new, Collectors.toList()));

			List<CategoryDTO.SubCategoryDTO> subDtoList = subCategories.entrySet().stream().map(subEntry -> {
				String subCode = subEntry.getKey();
				String subCategory = subEntry.getValue().get(0).getSubCategory();

				// 소분류 리스트
				List<CategoryDTO.DetailCategoryDTO> detailDTOList = subEntry.getValue().stream()
						.filter(c -> c.getDetailCategory() != null)
						.map(c -> new CategoryDTO.DetailCategoryDTO(c.getDetailCategory())).toList();

				return new CategoryDTO.SubCategoryDTO(subCategory, subCode, detailDTOList);
			}).toList();

			return new CategoryDTO(mainCategory, mainCode, subDtoList);

		}).toList();

	}
}
