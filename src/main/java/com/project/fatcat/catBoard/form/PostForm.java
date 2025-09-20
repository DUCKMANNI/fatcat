package com.project.fatcat.catboard.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostForm {
	
    @NotEmpty(message = "제목을 입력해주세요.")
    @Size(max = 200)
    private String postTitle;   // 엔티티랑 맞춤

    @NotEmpty(message = "내용을 입력해주세요.")
    private String postContent; // 엔티티랑 맞춤

   
}

