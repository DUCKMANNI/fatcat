package com.project.fatcat.chat.dto;

import java.time.LocalDateTime;

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
    private String type;
    private String content;

    
    private String startDate; 
    private String endDate; 
    private String status;
    private String note;
    private Integer sessionId;
    private LocalDateTime timestamp;
}