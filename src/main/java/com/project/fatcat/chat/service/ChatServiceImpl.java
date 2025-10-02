package com.project.fatcat.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.fatcat.care.dto.CareSessionDto;
import com.project.fatcat.care.service.CareSessionService;
import com.project.fatcat.chat.dto.ChatMessageDto;
import com.project.fatcat.chat.repository.CareChatHistoryRepository;
import com.project.fatcat.chat.repository.ChatRoomRepository;
import com.project.fatcat.entity.CareChatHistory;
import com.project.fatcat.entity.CareChatRoom;
import com.project.fatcat.entity.User; // ⭐ 가정: User 엔티티 임포트
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final CareChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;
    
    private final CareSessionService careSessionService; 

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ⭐ 변경됨: userName을 DB에서 조회하는 헬퍼 메서드 ⭐
    private String getUserName(Integer userSeq) {
        if (userSeq == null) return "Unknown User";
        
        // [주의] User 엔티티에 getUserName() 메서드가 있다고 가정하고 닉네임 대신 사용합니다.
        return userRepository.findById(userSeq)
                             .map(user -> user.getUserName()) // ⭐ 변경됨: getNickname() -> getUserName()
                             .orElse("User " + userSeq);
    }
    // ⭐ END: userName 헬퍼 메서드 ⭐
    
 // ✅ 추가: 사용자 프로필 이미지 URL을 조회하는 헬퍼 메서드
    public String getUserProfileImage(Integer userSeq) {
        if (userSeq == null) return null; // 또는 기본 이미지 URL을 반환
        
        return userRepository.findById(userSeq)
                             .map(user -> user.getProfileImage()) // User 엔티티의 profileImage 필드를 사용
                             .orElse(null); // 사용자가 없거나 이미지가 없으면 null 반환
    }

    @Override
    // senderId, receiverId -> senderSeq, receiverSeq로 변경
    public CareChatRoom getOrCreateChatRoom(Integer senderSeq, Integer receiverSeq) { 
        String chatName = Math.min(senderSeq, receiverSeq) + "-" + Math.max(senderSeq, receiverSeq);
        Optional<CareChatRoom> existingRoom = chatRoomRepository.findByChatName(chatName);

        return existingRoom.orElseGet(() -> {
            CareChatRoom newRoom = new CareChatRoom();
            newRoom.setChatName(chatName);
            newRoom.setChatMembers(2);
            newRoom.setCreateDate(LocalDateTime.now());
            return chatRoomRepository.save(newRoom);
        });
    }

    @Override
    public List<ChatMessageDto> getChatHistory(Integer chatRoomId) {
        CareChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);
        if (chatRoom != null) {
            List<CareChatHistory> history = chatHistoryRepository.findByCareChatRoomOrderByChatDateAsc(chatRoom);
            return history.stream()
                          .map(this::convertToDtoWithLatestStatus) 
                          .collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        // DTO 복사본을 만들어 saveMessage에 전달하여 원본 DTO를 건드리지 않음
        ChatMessageDto dtoToSave = new ChatMessageDto(chatMessageDto);
        
        // ⭐ 변경됨: userName을 DTO에 설정 (메시지 전송 직전에 DB 조회)
        dtoToSave.setSenderUserName(getUserName(dtoToSave.getSenderId())); // ⭐ 변경됨
        dtoToSave.setReceiverUserName(getUserName(dtoToSave.getReceiverId())); // ⭐ 변경됨
     
        
        CareChatHistory chatHistory = convertToEntity(dtoToSave);
        chatHistory.setChatDate(LocalDateTime.now());
        CareChatHistory savedHistory = chatHistoryRepository.save(chatHistory);
        
        // convertToDto 호출 시 프로필 이미지를 설정합니다.
        ChatMessageDto savedDto = convertToDto(savedHistory);
        
        // CARE_CONFIRM의 경우, 확정 시간을 savedDto에 설정하여 웹소켓으로 반환
        if ("CARE_CONFIRM".equals(savedDto.getType()) && savedDto.getSessionId() != null) {
            // 컨트롤러에서 설정한 값이 JSON 직렬화 시 포함되었거나, DB에서 다시 조회 (안전성 확보)
            CareSessionDto confirmedSession = careSessionService.getSessionById(savedDto.getSessionId()); 
            if (confirmedSession != null && confirmedSession.getConfirmedDate() != null) {
                // 이 부분을 직접 포맷할 필요는 없습니다. Jackson이 DTO의 @JsonFormat에 따라 처리합니다.
                savedDto.setConfirmedTime(confirmedSession.getConfirmedDate().toString());
            }
        }
        
        return savedDto;
    }

    // 최신 CareSession 상태 반영 메서드 (히스토리 조회 시 사용)
    private ChatMessageDto convertToDtoWithLatestStatus(CareChatHistory entity) {
        ChatMessageDto dto = convertToDto(entity); 

        if ("CARE_REQUEST".equals(dto.getType()) && dto.getSessionId() != null) {
            
            CareSessionDto sessionDto = careSessionService.getSessionById(dto.getSessionId()); 

            if (sessionDto != null) {
                dto.setStatus(sessionDto.getStatus());
                
                if ("CONFIRMED".equals(sessionDto.getStatus()) && sessionDto.getConfirmedDate() != null) {
                    dto.setConfirmedTime(sessionDto.getConfirmedDate().toString());
                }
            }
        }
        return dto;
    }

    // --- DTO → 엔티티 변환 (Data Truncation 방지 로직 수정) ---
    private CareChatHistory convertToEntity(ChatMessageDto dto) {
    	
    	 // 💡 오류 방지 로직 추가: chatRoomId가 null이면 예외 발생
        if (dto.getChatRoomId() == null) {
            throw new IllegalArgumentException("ChatRoom ID가 null이므로 메시지를 저장할 수 없습니다. (DTO: " + dto.toString() + ")");
        }
        
        CareChatHistory entity = new CareChatHistory();
        
        // chatRoomRepository.findById(null) 호출 방지
        CareChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId())
                                                 .orElseThrow(() -> new RuntimeException("ChatRoom ID " + dto.getChatRoomId() + "를 찾을 수 없습니다."));

        // chatSender는 String 타입이므로, userSeq를 String으로 변환
        entity.setChatSender(String.valueOf(dto.getSenderId())); 
        entity.setCareChatRoom(chatRoom);

        String messageType = dto.getType();
        if (messageType == null || messageType.isEmpty()) {
            messageType = "CHAT"; 
        }
        entity.setMessageType(messageType); 
        
        if (dto.getSessionId() != null) {
            entity.setSessionId(dto.getSessionId());
            entity.setCareStatus(dto.getStatus()); 
        }

        // startDate, endDate를 JSON 필드에 그대로 저장하여 히스토리 조회 시 사용합니다.
        if ("CARE_REQUEST".equals(dto.getType())) {
            dto.setConfirmedTime(null);
        }

        try {
            if ("CARE_REQUEST".equals(dto.getType()) || "CARE_CONFIRM".equals(dto.getType())) {
                // CARE 관련 메시지는 길이 최적화된 DTO를 JSON 직렬화
                entity.setChatMessage(objectMapper.writeValueAsString(dto));
            } else {
                entity.setChatMessage(dto.getContent());
            }
        } catch (Exception e) {
            throw new RuntimeException("메시지 직렬화 실패", e);
        }

        return entity;
    }

    // --- 엔티티 → DTO 변환 (Sender/Receiver 프로필 이미지 조회 및 보존 로직 추가) ---
    private ChatMessageDto convertToDto(CareChatHistory entity) {
        ChatMessageDto dto = new ChatMessageDto();

        dto.setChatRoomId(entity.getCareChatRoom().getChatSeq());
        // String sender를 Integer userSeq로 변환
        Integer senderSeq = Integer.parseInt(entity.getChatSender());
        dto.setSenderId(senderSeq); 
        // ⭐ 변경됨: userName 설정 (DB 조회)
        dto.setSenderUserName(getUserName(senderSeq)); 

        String chatName = entity.getCareChatRoom().getChatName();
        String[] ids = chatName.split("-");
        Integer member1 = Integer.parseInt(ids[0]);
        Integer member2 = Integer.parseInt(ids[1]);

        // receiverId도 userSeq를 기반으로 결정
        Integer receiverSeq = senderSeq.equals(member1) ? member2 : member1;
        dto.setReceiverId(receiverSeq);
        // ⭐ 변경됨: userName 설정 (DB 조회)
        dto.setReceiverUserName(getUserName(receiverSeq));

        dto.setTimestamp(entity.getChatDate());
        
        // ✅ 1. 보낸 사람(Sender) 프로필 이미지 DB에서 가져오기 (⭐ 추가됨)
        userRepository.findById(senderSeq).ifPresent(user -> dto.setSenderProfileImage(user.getProfileImage())); 
        
        // ✅ 2. 받는 사람(Receiver) 프로필 이미지 DB에서 가져오기 (기존 로직)
        userRepository.findById(receiverSeq).ifPresent(user -> dto.setReceiverProfileImage(user.getProfileImage()));
        
        // ⭐⭐ [핵심 수정 1] JSON 역직렬화 시 손실 방지를 위해 프로필 URL을 임시 변수에 저장합니다. ⭐⭐
        String receiverProfileImageUrl = dto.getReceiverProfileImage();
        String senderProfileImageUrl = dto.getSenderProfileImage(); 


        try {
            String rawMessage = entity.getChatMessage();
            if (rawMessage != null && rawMessage.trim().startsWith("{")) {
                // JSON → DTO 역직렬화
                ChatMessageDto payload = objectMapper.readValue(rawMessage, ChatMessageDto.class);

                dto.setType(payload.getType());
                dto.setContent(payload.getContent());
                dto.setSessionId(payload.getSessionId());
                dto.setStatus(payload.getStatus());
                dto.setStartDate(payload.getStartDate());
                dto.setEndDate(payload.getEndDate());
                dto.setNote(payload.getNote());
                dto.setConfirmedTime(payload.getConfirmedTime());
                
                // ⭐ 변경됨: CARE_REQUEST/CONFIRM DTO에도 userName이 반영되도록 처리
                if(payload.getSenderUserName() != null) dto.setSenderUserName(payload.getSenderUserName());
                if(payload.getReceiverUserName() != null) dto.setReceiverUserName(payload.getReceiverUserName());
                
                // ⭐⭐ [핵심 수정 2] JSON 역직렬화 후, 임시 변수에 저장했던 프로필 이미지들을 다시 설정합니다. ⭐⭐
                dto.setReceiverProfileImage(receiverProfileImageUrl);
                dto.setSenderProfileImage(senderProfileImageUrl);
            } else {
                // 일반 채팅
                dto.setType(entity.getMessageType() != null ? entity.getMessageType() : "CHAT"); 
                dto.setContent(rawMessage);
            }
        } catch (Exception e) {
            dto.setType(entity.getMessageType() != null ? entity.getMessageType() : "CHAT");
            dto.setContent(entity.getChatMessage());
        }
        
        
        
        // ⭐⭐⭐ 최종 전송 값 확인용 디버깅 로그 (필요 없으면 삭제하세요) ⭐⭐⭐
        System.out.println("DEBUG (ChatService): ChatRoom ID " + dto.getChatRoomId() 
                            + " / Sender ID " + dto.getSenderId() 
                            + " / Sender Profile URL: " + dto.getSenderProfileImage());
        System.out.println("DEBUG (ChatService): ChatRoom ID " + dto.getChatRoomId() 
                            + " / Receiver ID " + dto.getReceiverId() 
                            + " / Receiver Profile URL: " + dto.getReceiverProfileImage()); 

        return dto;
    }
}