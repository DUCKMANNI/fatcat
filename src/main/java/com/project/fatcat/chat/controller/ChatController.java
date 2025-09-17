package com.project.fatcat.chat.controller;

import com.project.fatcat.chat.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/private-chat")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
        System.out.println("메시지 수신 - 발신자: " + chatMessage.getSender() + ", 내용: " + chatMessage.getContent());

        messagingTemplate.convertAndSendToUser(
            chatMessage.getReceiver(),
            "/private",
            chatMessage
        );

        messagingTemplate.convertAndSendToUser(
            chatMessage.getSender(),
            "/private",
            chatMessage
        );
    }
}