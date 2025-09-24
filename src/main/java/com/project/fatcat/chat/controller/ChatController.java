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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private CareSessionService careSessionService;

    @MessageMapping("/private-chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        
        // 날짜/시간 포맷터 정의 (프론트엔드 형식과 일치)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // 1. 메시지 타입별 비즈니스 로직 처리
        switch (chatMessageDto.getType()) {
            case "CARE_REQUEST":
                
                // String 타입으로 받은 날짜를 LocalDateTime으로 직접 파싱
                LocalDateTime startDate = LocalDateTime.parse(chatMessageDto.getStartDate(), formatter);
                LocalDateTime endDate = LocalDateTime.parse(chatMessageDto.getEndDate(), formatter);

                CareSessionDto request = CareSessionDto.builder()
                        .ownerUserId(chatMessageDto.getSenderId())
                        .sitterUserId(chatMessageDto.getReceiverId())
                        .startDate(startDate) // 파싱된 LocalDateTime 객체 사용
                        .endDate(endDate) // 파싱된 LocalDateTime 객체 사용
                        .status("REQUESTED")
                        .note(chatMessageDto.getNote())
                        .build();

                CareSessionDto savedRequest = careSessionService.createSession(request);
                
                // LocalDateTime을 프론트엔드에 표시할 문자열로 포맷팅
                DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedStartDate = savedRequest.getStartDate().format(displayFormatter);
                String formattedEndDate = savedRequest.getEndDate().format(displayFormatter);
                
                chatMessageDto.setSessionId(savedRequest.getId());
                chatMessageDto.setContent("📌 돌봄 요청: " + formattedStartDate + " ~ " + formattedEndDate);
                break;

            case "CARE_CONFIRM":
                careSessionService.confirmSession(chatMessageDto.getSessionId());
                chatMessageDto.setContent("✅ 돌봄 예약이 확정되었습니다.");
                break;

            default:
                break;
        }

        chatService.saveMessage(chatMessageDto);

        if (chatMessageDto.getChatRoomId() != null) {
            messagingTemplate.convertAndSend(
                    "/topic/chat/" + chatMessageDto.getChatRoomId(),
                    chatMessageDto
            );
        }
    }

    @GetMapping("/chat/start")
    public String startChat(Model model) {
        return "chat/chat_form";
    }

    @GetMapping("/api/chat/room")
    @ResponseBody
    public Object getOrCreateRoom(@RequestParam("sender") Integer senderId,
                                  @RequestParam("receiver") Integer receiverId) {
        return chatService.getOrCreateChatRoom(senderId, receiverId);
    }

    @GetMapping("/api/chat/history")
    @ResponseBody
    public Object getChatHistory(@RequestParam("roomId") Integer chatRoomId) {
        return chatService.getChatHistory(chatRoomId);
    }
}