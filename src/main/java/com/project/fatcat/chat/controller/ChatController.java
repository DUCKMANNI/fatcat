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
        
        // 클라이언트에서 넘어온 T가 포함된 ISO 형식의 문자열을 파싱하는 포맷터
        DateTimeFormatter isoParserFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        
        // DTO의 String 필드에 저장할 때 사용하며, DTO의 @JsonFormat과 일치해야 합니다.
        DateTimeFormatter isoOutputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        switch (chatMessageDto.getType()) {
            case "CARE_REQUEST":
                // 1. DTO에서 String을 LocalDateTime으로 파싱
                LocalDateTime startDate = LocalDateTime.parse(chatMessageDto.getStartDate(), isoParserFormatter);
                LocalDateTime endDate = LocalDateTime.parse(chatMessageDto.getEndDate(), isoParserFormatter);

                CareSessionDto request = CareSessionDto.builder()
                        .ownerUserId(chatMessageDto.getSenderId())
                        .sitterUserId(chatMessageDto.getReceiverId())
                        .startDate(startDate)
                        .endDate(endDate)
                        .status("REQUESTED")
                        .note(chatMessageDto.getNote())
                        .build();

                CareSessionDto savedRequest = careSessionService.createSession(request);

                // 2. Content에 표시할 사용자 친화적 포맷터
                DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedStartDate = savedRequest.getStartDate().format(displayFormatter);
                String formattedEndDate = savedRequest.getEndDate().format(displayFormatter);

                chatMessageDto.setSessionId(savedRequest.getId());
                chatMessageDto.setStatus("REQUESTED");
                
                // ⭐ 수정된 부분: LocalDateTime.toString() 대신 명시적 포맷터 사용 ⭐
                // DTO의 @JsonFormat 패턴(yyyy-MM-dd'T'HH:mm:ss)에 정확히 맞춥니다.
                chatMessageDto.setStartDate(savedRequest.getStartDate().format(isoOutputFormatter)); 
                chatMessageDto.setEndDate(savedRequest.getEndDate().format(isoOutputFormatter));
                
                chatMessageDto.setContent("📌 돌봄 요청: " + formattedStartDate + " ~ " + formattedEndDate);
                break;
                
            case "CARE_CONFIRM":
                CareSessionDto confirmedSession = careSessionService.confirmSession(chatMessageDto.getSessionId());

                chatMessageDto.setStatus("CONFIRMED");
                chatMessageDto.setContent("✅ 돌봄 예약이 확정되었습니다.");
                
                // DTO의 timestamp와 confirmedTime은 'yyyy-MM-dd HH:mm:ss' 형식이므로,
                // 이 역시 명시적 포맷터로 처리하는 것이 좋습니다.
                DateTimeFormatter confirmOutputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if (confirmedSession.getConfirmedDate() != null) {
                    // DTO의 confirmedTime은 String 타입이므로 format() 사용
                    chatMessageDto.setConfirmedTime(confirmedSession.getConfirmedDate().format(confirmOutputFormatter)); 
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