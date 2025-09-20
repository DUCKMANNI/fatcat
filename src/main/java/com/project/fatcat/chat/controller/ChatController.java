package com.project.fatcat.chat.controller;

import com.project.fatcat.chat.dto.ChatMessageDto;
import com.project.fatcat.chat.service.ChatService;
import com.project.fatcat.entity.CareChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    // 임시로 userSeq 1을 현재 로그인한 사용자로 가정합니다.
    private static final Integer CURRENT_USER_ID = 1;

    @GetMapping("/chat/start")
    public String startChat(@RequestParam("receiverSeq") Integer receiverSeq, Model model) {
        CareChatRoom chatRoom = chatService.getOrCreateChatRoom(CURRENT_USER_ID, receiverSeq);

        model.addAttribute("chatRoomId", chatRoom.getChatSeq());
        model.addAttribute("senderId", CURRENT_USER_ID);
        model.addAttribute("receiverId", receiverSeq);

        return "chat/chat_form"; // 👈 이 부분을 chat_form으로 변경합니다.
    }

    @MessageMapping("/private-chat")
    public void sendPrivateMessage(@Payload ChatMessageDto chatMessageDto) {
        ChatMessageDto savedDto = chatService.saveMessage(chatMessageDto);
        
        messagingTemplate.convertAndSendToUser(
            String.valueOf(savedDto.getSenderId()),
            "/private",
            savedDto
        );
        messagingTemplate.convertAndSendToUser(
            // 수신자 ID를 메시지 DTO에 추가로 포함시켜 보내는 게 좋습니다.
            // 여기서는 예시로 receiverId를 사용
            String.valueOf(chatMessageDto.getRecieverId()), 
            "/private",
            savedDto
        );
    }
}