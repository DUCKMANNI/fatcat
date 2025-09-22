package com.project.fatcat.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.service.CareSessionService;
import com.project.fatcat.chat.dto.ChatMessageDto;
import com.project.fatcat.chat.service.ChatService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private CareSessionService careSessionService;

    // ----------------------------
    // WebSocket 메시지 처리
    // ----------------------------
    @MessageMapping("/private-chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        // 1️⃣ CARE_CONFIRM 타입 처리 (돌봄 예약)
        if ("CARE_CONFIRM".equalsIgnoreCase(chatMessageDto.getType())) {
            CareSessionDto sessionDto = CareSessionDto.builder()
                    .ownerUserId(chatMessageDto.getReceiverId())
                    .sitterUserId(chatMessageDto.getSenderId())
                    .startDate(chatMessageDto.getStartDate())
                    .endDate(chatMessageDto.getEndDate())
                    .status("CONFIRMED")
                    .build();

            CareSessionDto saved = careSessionService.createSession(sessionDto);
            System.out.println("✅ CareSession saved with ID: " + saved.getId());
        }

        // 2️⃣ 모든 경우에 방 브로드캐스트
        if (chatMessageDto.getChatRoomId() != null) {
            messagingTemplate.convertAndSend(
                    "/topic/chat/" + chatMessageDto.getChatRoomId(),
                    chatMessageDto
            );
        }
    }

    // ----------------------------
    // 채팅 페이지 렌더링
    // ----------------------------
    @GetMapping("/chat/start")
    public String startChat(Model model) {
        return "chat/chat_form";
    }

    // ----------------------------
    // REST API: sender/receiver로 chatRoom 조회 또는 생성
    // ----------------------------
    @GetMapping("/api/chat/room")
    @ResponseBody
    public Object getOrCreateRoom(@RequestParam("sender") Integer senderId,
                                  @RequestParam("receiver") Integer receiverId) {
        return chatService.getOrCreateChatRoom(senderId, receiverId);
    }

    // ----------------------------
    // REST API: 채팅 기록 가져오기
    // ----------------------------
    @GetMapping("/api/chat/history")
    @ResponseBody
    public Object getChatHistory(@RequestParam("roomId") Integer chatRoomId) {
        return chatService.getChatHistory(chatRoomId);
    }
}
