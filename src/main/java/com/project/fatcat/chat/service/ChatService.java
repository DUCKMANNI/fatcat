package com.project.fatcat.chat.service;

import com.project.fatcat.chat.dto.ChatMessageDto; // DTO 임포트
import com.project.fatcat.entity.CareChatRoom;
import java.util.List;

public interface ChatService {
    CareChatRoom getOrCreateChatRoom(Integer senderId, Integer receiverId);
    List<ChatMessageDto> getChatHistory(Integer chatRoomId); 
    ChatMessageDto saveMessage(ChatMessageDto chatMessageDto); 
    
}