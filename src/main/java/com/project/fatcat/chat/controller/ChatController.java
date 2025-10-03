package com.project.fatcat.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.fatcat.SecurityUtils;
import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.service.CareSessionService;
import com.project.fatcat.chat.dto.ChatMessageDto;
import com.project.fatcat.chat.service.ChatService;
import com.project.fatcat.users.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class ChatController {
	
	@Autowired
	private UserService userService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private CareSessionService careSessionService;

    @MessageMapping("/private-chat")
    public void processMessage(@Payload ChatMessageDto chatMessageDto) {
        
        // yyyy-MM-dd'T'HH:mm:ss 형태의 문자열을 파싱합니다.
        DateTimeFormatter isoParserFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter isoOutputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        
        // chatMessageDto.getSenderId()와 .getReceiverId()는 userSeq 값을 가집니다.

        switch (chatMessageDto.getType()) {
            case "CARE_REQUEST":
                LocalDateTime startDate = LocalDateTime.parse(chatMessageDto.getStartDate(), isoParserFormatter);
                LocalDateTime endDate = LocalDateTime.parse(chatMessageDto.getEndDate(), isoParserFormatter);

                CareSessionDto request = CareSessionDto.builder()
                        // ownerUserId/sitterUserId 필드가 userSeq를 받습니다.
                        .ownerUserId(chatMessageDto.getSenderId()) 
                        .sitterUserId(chatMessageDto.getReceiverId())
                        .startDate(startDate)
                        .endDate(endDate)
                        .status("REQUESTED")
                        .note(chatMessageDto.getNote())
                        .careBoardSeq(chatMessageDto.getCareSeq()) // 💡 [추가] CareSeq 설정
                        .build();

                CareSessionDto savedRequest = careSessionService.createSession(request);
                
                DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedStartDate = savedRequest.getStartDate().format(displayFormatter);
                String formattedEndDate = savedRequest.getEndDate().format(displayFormatter);

                chatMessageDto.setSessionId(savedRequest.getId());
                chatMessageDto.setStatus("REQUESTED");
                
                chatMessageDto.setStartDate(savedRequest.getStartDate().format(isoOutputFormatter)); 
                chatMessageDto.setEndDate(savedRequest.getEndDate().format(isoOutputFormatter));
                
                chatMessageDto.setContent("📌 돌봄 요청: " + formattedStartDate + " ~ " + formattedEndDate);
                break;
                
            case "CARE_CONFIRM":
                CareSessionDto confirmedSession = careSessionService.confirmSession(chatMessageDto.getSessionId());

                chatMessageDto.setStatus("CONFIRMED");
                chatMessageDto.setContent("✅ 돌봄 예약이 확정되었습니다.");
                
                DateTimeFormatter confirmOutputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if (confirmedSession.getConfirmedDate() != null) {
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

    /**
     * 채팅 화면을 보여주는 컨트롤러
     * userSeq를 Integer 타입으로 통일하고, /chat/start 경로와 receiverSeq 파라미터 이름을 사용합니다.
     * 💡 loggedInUserSeq 파라미터를 제거하고 SecurityUtils에서 값을 가져와 Spring Argument Resolution 오류를 해결합니다.
     */
    @GetMapping("/chat/start")
    public String showChatForm(
            @RequestParam(name = "receiverSeq", required = true) Integer targetUserSeq, 
            Model model
        ) {
        
        // 💡 SecurityUtils를 사용하여 현재 로그인된 사용자 ID를 가져옵니다.
        Integer loggedInUserSeq = SecurityUtils.getCurrentUserSeq(); 
        
        // [주의]: 실제 환경에서는 이 임시 코드를 반드시 제거하고 인증 로직을 완성해야 합니다.
        if (loggedInUserSeq == null) {
            System.out.println("DEBUG: SecurityUtils에서 사용자 ID를 찾을 수 없습니다. 임시 ID 10을 사용합니다.");
            loggedInUserSeq = 10; // 임시 로그인 사용자 ID (Integer)
        }
        
        	String receiverProfileImage = chatService.getUserProfileImage(targetUserSeq);
        	
        // Model에 두 사용자 ID를 담아 뷰로 전달 (targetUserSeq는 receiverSeq와 동일)
        model.addAttribute("loggedInUserSeq", loggedInUserSeq);
        model.addAttribute("targetUserSeq", targetUserSeq); 
        
    
        
     // ⭐ 변경됨: 'receiverProfileImage' 변수를 'targetUserProfileImage'라는 이름으로 모델에 추가
        model.addAttribute("targetUserProfileImage", receiverProfileImage != null ? receiverProfileImage : "/images/user_no_image.jpg");

        
		return "chat/chat_form"; 
	}

    /**
     * 채팅방 ID를 조회하거나 새로 생성합니다.
     * senderSeq를 쿼리 파라미터로 받는 대신, SecurityUtils를 사용해 로그인된 사용자 ID를 자동으로 가져옵니다.
     * 이렇게 하면 클라이언트가 'sender' 정보를 조작할 위험을 방지하고 인증 정보를 신뢰할 수 있습니다.
     */
    @GetMapping("/api/chat/room")
    @ResponseBody
    public Object getOrCreateRoom(
        @RequestParam("receiver") Integer receiverSeq) { // receiverSeq(대화 상대)만 쿼리 파라미터로 받습니다.
        
        // 💡 SecurityUtils를 사용하여 현재 로그인된 사용자 ID(senderSeq)를 가져옵니다.
        Integer senderSeq = SecurityUtils.getCurrentUserSeq();

        // [주의]: 실제 환경에서는 이 임시 코드를 반드시 제거하고 인증 로직을 완성해야 합니다.
        if (senderSeq == null) {
            System.err.println("ERROR: API 호출 시 인증된 사용자(sender) ID를 찾을 수 없습니다.");
            senderSeq = 10; // 임시 로그인 사용자 ID (Integer)
        }
        
        if (senderSeq.equals(receiverSeq)) {
            throw new IllegalArgumentException("본인과는 채팅방을 만들 수 없습니다.");
        }
        
        return chatService.getOrCreateChatRoom(senderSeq, receiverSeq); 
    }

    @GetMapping("/api/chat/history")
    @ResponseBody
    public Object getChatHistory(@RequestParam("roomId") Integer chatRoomId) {
        return chatService.getChatHistory(chatRoomId);
    }
    
    @GetMapping("/api/users/{userSeq}/userName") // 💡 클라이언트가 요청하는 정확한 경로입니다.
    @ResponseBody
    public Object getUserNameBySeq(@PathVariable("userSeq") Integer userSeq) {
        
        // 1. UserService를 사용하여 User 객체를 조회합니다. (예: userDto는 이름을 포함하고 있어야 합니다.)
        // 2. 실제 UserService 메서드명과 반환 타입은 프로젝트에 맞게 수정해야 합니다.
        String userName = userService.getUserNickNameBySeq(userSeq); 

        // 3. 클라이언트가 JSON 객체로 받기 때문에, Map 형태로 구성하여 반환합니다.
        //    클라이언트 JavaScript는 data.userName을 기대하고 있습니다.
        return java.util.Collections.singletonMap("userName", userName);
    }
}
