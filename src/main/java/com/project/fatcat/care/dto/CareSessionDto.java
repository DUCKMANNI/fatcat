package com.project.fatcat.care.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareSessionDto {
    private Integer id;
    private Integer ownerUserId;
    private Integer sitterUserId;
    private String note;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDate;

    private String status; // "CONFIRMED", "PENDING", "CANCELLED" 등
    
    // ⭐ ⭐ ⭐ 추가된 필드: 확정 시간 ⭐ ⭐ ⭐
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // DB 저장 및 JSON 응답 포맷
    private LocalDateTime confirmedDate;
}