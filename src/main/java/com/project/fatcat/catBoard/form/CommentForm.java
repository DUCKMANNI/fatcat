package com.project.fatcat.catBoard.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentForm {
	


    @NotEmpty(message = "내용을 입력해주세요.")
    private String commentContent;
   
}
