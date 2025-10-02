package com.project.fatcat.inquiry.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InquiryForm {

	 // Inquiry 엔티티의 inquiryTitle에 해당
	@NotEmpty(message = "제목은 필수 입니다")
    private String inquiryTitle;
    
    // Inquiry 엔티티의 inquiryContent에 해당
	@NotEmpty(message = "내용은 필수 입니다")
    private String inquiryContent;
    
    // 참고: validation(@NotBlank 등)이 필요하면 여기에 추가합니다.
	
}
