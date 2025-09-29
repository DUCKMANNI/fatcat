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
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        switch (chatMessageDto.getType()) {
            case "CARE_REQUEST":
                LocalDateTime startDate = LocalDateTime.parse(chatMessageDto.getStartDate(), formatter);
                LocalDateTime endDate = LocalDateTime.parse(chatMessageDto.getEndDate(), formatter);

                CareSessionDto request = CareSessionDto.builder()
                        .ownerUserId(chatMessageDto.getSenderId())
                        .sitterUserId(chatMessageDto.getReceiverId())
                        .startDate(startDate)
                        .endDate(endDate)
                        .status("REQUESTED")
                        .note(chatMessageDto.getNote())
                        .build();

                CareSessionDto savedRequest = careSessionService.createSession(request);

                DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedStartDate = savedRequest.getStartDate().format(displayFormatter);
                String formattedEndDate = savedRequest.getEndDate().format(displayFormatter);

                chatMessageDto.setSessionId(savedRequest.getId());
                chatMessageDto.setStatus("REQUESTED");
                
                chatMessageDto.setStartDate(savedRequest.getStartDate().toString()); 
                chatMessageDto.setEndDate(savedRequest.getEndDate().toString());
                chatMessageDto.setContent("📌 돌봄 요청: " + formattedStartDate + " ~ " + formattedEndDate);
                break;
                
            case "CARE_CONFIRM":
                // ⭐ 수정: confirmSession이 CareSessionDto를 반환한다고 가정하고 확정 시간을 얻어옵니다.
                CareSessionDto confirmedSession = careSessionService.confirmSession(chatMessageDto.getSessionId());

                chatMessageDto.setStatus("CONFIRMED");
                chatMessageDto.setContent("✅ 돌봄 예약이 확정되었습니다.");
                
                // ⭐ ⭐ ⭐ 추가: 확정 시간을 DTO에 설정 ⭐ ⭐ ⭐
                if (confirmedSession.getConfirmedDate() != null) {
                    // DTO의 confirmedTime은 String 타입이므로 toString() 사용
                    chatMessageDto.setConfirmedTime(confirmedSession.getConfirmedDate().toString()); 
                }
                break;

            default:
                break;
        }

        ChatMessageDto savedDto = chatService.saveMessage(chatMessageDto);

     if (savedDto.getChatRoomId() != null) {
         messagingTemplate.convertAndSend(
                 "/topic/chat/" + savedDto.getChatRoomId(),
                 savedDto
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