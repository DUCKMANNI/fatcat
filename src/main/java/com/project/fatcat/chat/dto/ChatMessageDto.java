package com.project.fatcat.chat.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Integer chatRoomId;
    private Integer senderId;
    private Integer receiverId;
    private String type;      // "CHAT", "CARE_REQUEST", "CARE_CONFIRM"
    private String content;

    // 돌봄 요청 관련 필드
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String startDate;   // 프론트에서 보낸 원본
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String endDate;
    private String status;      // "REQUESTED", "CONFIRMED", "CANCELLED"
    private String note;
    private Integer sessionId;

    // 저장된 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    // ⭐ ⭐ ⭐ 추가된 필드: 확정 시간 ⭐ ⭐ ⭐
    // CareSession CONFIRMED 상태일 때 프론트엔드 UI 업데이트를 위해 사용됩니다.
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String confirmedTime;
    
    public ChatMessageDto(ChatMessageDto other) {
        this.chatRoomId = other.chatRoomId;
        this.senderId = other.senderId;
        this.receiverId = other.receiverId;
        this.type = other.type;
        this.content = other.content;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.status = other.status;
        this.note = other.note;
        this.sessionId = other.sessionId;
        this.timestamp = other.timestamp;
        this.confirmedTime = other.confirmedTime;
    }
}