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
    private String senderUserName;   // ⭐ 변경됨: 보내는 사람 userName
    private Integer receiverId;
    private String receiverUserName; // ⭐ 변경됨: 받는 사람 userName
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
    
    // 추가된 필드: 확정 시간
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String confirmedTime;
    
 // ✅ 추가
    private String senderProfileImage;
    private String receiverProfileImage;
    
    public ChatMessageDto(ChatMessageDto other) {
        this.chatRoomId = other.chatRoomId;
        this.senderId = other.senderId;
        this.senderUserName = other.senderUserName; // ⭐ 변경됨
        this.receiverId = other.receiverId;
        this.receiverUserName = other.receiverUserName; // ⭐ 변경됨
        this.type = other.type;
        this.content = other.content;
        this.startDate = other.startDate;
        this.endDate = other.endDate;
        this.status = other.status;
        this.note = other.note;
        this.sessionId = other.sessionId;
        this.timestamp = other.timestamp;
        this.confirmedTime = other.confirmedTime;
        this.senderProfileImage = other.senderProfileImage;
        this.receiverProfileImage = other.receiverProfileImage;
    }
}