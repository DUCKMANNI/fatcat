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
        
        // 날짜/시간 파싱 포맷 (프론트와 맞춤)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        switch (chatMessageDto.getType()) {
            case "CARE_REQUEST":
                // 받은 날짜 문자열 → LocalDateTime 변환
                LocalDateTime startDate = LocalDateTime.parse(chatMessageDto.getStartDate(), formatter);
                LocalDateTime endDate = LocalDateTime.parse(chatMessageDto.getEndDate(), formatter);

                // CareSession 저장
                CareSessionDto request = CareSessionDto.builder()
                        .ownerUserId(chatMessageDto.getSenderId())
                        .sitterUserId(chatMessageDto.getReceiverId())
                        .startDate(startDate)
                        .endDate(endDate)
                        .status("REQUESTED")
                        .note(chatMessageDto.getNote())
                        .build();

                CareSessionDto savedRequest = careSessionService.createSession(request);

                // 프론트 표시용 날짜 포맷
                DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedStartDate = savedRequest.getStartDate().format(displayFormatter);
                String formattedEndDate = savedRequest.getEndDate().format(displayFormatter);

                // 👉 DTO에 세션 관련 정보 저장 (DB JSON 직렬화용)
                chatMessageDto.setSessionId(savedRequest.getId());
                chatMessageDto.setStatus("REQUESTED");
                chatMessageDto.setStartDate(savedRequest.getStartDate().toString());
                chatMessageDto.setEndDate(savedRequest.getEndDate().toString());
                chatMessageDto.setContent("📌 돌봄 요청: " + formattedStartDate + " ~ " + formattedEndDate);
                break;

            case "CARE_CONFIRM":
                careSessionService.confirmSession(chatMessageDto.getSessionId());

                chatMessageDto.setStatus("CONFIRMED");
                chatMessageDto.setContent("✅ 돌봄 예약이 확정되었습니다.");
                break;

            default:
                break;
        }

        // DB 저장
        chatService.saveMessage(chatMessageDto);

        // 구독자들에게 전송
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
