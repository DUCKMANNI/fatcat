package com.project.fatcat.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Integer chatRoomId;
    private Integer senderId;
    private Integer recieverId;
    private String content;
    private LocalDateTime timestamp;
}