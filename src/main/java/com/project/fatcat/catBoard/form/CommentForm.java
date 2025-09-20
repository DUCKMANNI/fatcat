package com.project.fatcat.catboard.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentForm {
	
//	@NotEmpty(message = "댓글을 작성할 게시물을 선택해주세요.")
//    private Integer postSeq;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String commentContent;
   
}
