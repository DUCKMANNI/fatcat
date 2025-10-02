package com.project.fatcat.inquiry.dto;


import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InquiryListDTO {

    private Integer inquirySeq;
    private String inquiryTitle;
    private LocalDateTime createDate;
    private Integer viewCount;
    private Boolean isAnswered; // 답변 완료 여부 (화면 표시용)
}