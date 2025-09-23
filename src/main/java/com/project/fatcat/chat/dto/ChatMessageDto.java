package com.project.fatcat.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    private Integer chatRoomId;
    private Integer senderId;
    private Integer receiverId; 
    private LocalDateTime timestamp;
	private String content;
	
	private String type; //CARE_REQUEST같은 것들 
	
	@JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm")
	private LocalDateTime startDate;
	
	@JsonFormat(pattern = "yyyy-mm-dd'T'HH:mm")
	private LocalDateTime endDate;
}