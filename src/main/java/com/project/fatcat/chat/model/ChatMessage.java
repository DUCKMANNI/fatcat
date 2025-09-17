package com.project.fatcat.chat.model;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

	private String sender; //(메세지 보낸사람의 ID)
	
	private String receiver; //메세지 받는 사람 ID
	
	private String content; //메세지 내용
	
	
	
	public enum MessageType{
		CHAT, //일반 채팅 메세지
		JOIN, //채팅방 입장 알림
		LEAVE //채팅방 퇴장 알림 
	}
}
