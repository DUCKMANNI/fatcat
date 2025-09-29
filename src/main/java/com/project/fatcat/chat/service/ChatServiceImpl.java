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

    @Override
    public CareChatRoom getOrCreateChatRoom(Integer senderId, Integer receiverId) {
        String chatName = Math.min(senderId, receiverId) + "-" + Math.max(senderId, receiverId);
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
        
        CareChatHistory chatHistory = convertToEntity(dtoToSave);
        chatHistory.setChatDate(LocalDateTime.now());
        CareChatHistory savedHistory = chatHistoryRepository.save(chatHistory);
        
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
        CareChatHistory entity = new CareChatHistory();
        CareChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow();

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

        // ⭐ ⭐ ⭐ 수정됨: startDate, endDate를 null 처리하는 로직을 제거했습니다. ⭐ ⭐ ⭐
        // 이 정보를 DB의 chatMessage JSON 필드에 그대로 저장하여 히스토리 조회 시 사용할 수 있도록 합니다.
        if ("CARE_REQUEST".equals(dto.getType())) {
            dto.setConfirmedTime(null);
            // dto.setNote(null); // NOTE가 길다면 이것도 null 처리 고려
        }
        // ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ ⭐ 

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

    // --- 엔티티 → DTO 변환 (변경 없음) ---
    private ChatMessageDto convertToDto(CareChatHistory entity) {
        ChatMessageDto dto = new ChatMessageDto();

        dto.setChatRoomId(entity.getCareChatRoom().getChatSeq());
        dto.setSenderId(Integer.parseInt(entity.getChatSender()));

        String chatName = entity.getCareChatRoom().getChatName();
        String[] ids = chatName.split("-");
        Integer member1 = Integer.parseInt(ids[0]);
        Integer member2 = Integer.parseInt(ids[1]);

        dto.setReceiverId(Integer.parseInt(entity.getChatSender()) == member1 ? member2 : member1);
        dto.setTimestamp(entity.getChatDate());

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
            } else {
                // 일반 채팅
                dto.setType(entity.getMessageType() != null ? entity.getMessageType() : "CHAT"); 
                dto.setContent(rawMessage);
            }
        } catch (Exception e) {
            dto.setType(entity.getMessageType() != null ? entity.getMessageType() : "CHAT");
            dto.setContent(entity.getChatMessage());
        }

        return dto;
    }
}