package com.project.fatcat.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
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
            return history.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        CareChatHistory chatHistory = convertToEntity(chatMessageDto);
        chatHistory.setChatDate(LocalDateTime.now());
        CareChatHistory savedHistory = chatHistoryRepository.save(chatHistory);
        return convertToDto(savedHistory);
    }

    // --- DTO → 엔티티 변환 ---
    private CareChatHistory convertToEntity(ChatMessageDto dto) {
        CareChatHistory entity = new CareChatHistory();
        CareChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow();

        entity.setChatSender(String.valueOf(dto.getSenderId()));
        entity.setCareChatRoom(chatRoom);

        try {
            if ("CARE_REQUEST".equals(dto.getType()) || "CARE_CONFIRM".equals(dto.getType())) {
                // CARE 관련 메시지는 JSON 직렬화
                entity.setChatMessage(objectMapper.writeValueAsString(dto));
            } else {
                // 일반 채팅은 content만 저장
                entity.setChatMessage(dto.getContent());
            }
        } catch (Exception e) {
            throw new RuntimeException("메시지 직렬화 실패", e);
        }

        return entity;
    }

    // --- 엔티티 → DTO 변환 ---
    private ChatMessageDto convertToDto(CareChatHistory entity) {
        ChatMessageDto dto = new ChatMessageDto();

        dto.setChatRoomId(entity.getCareChatRoom().getChatSeq());
        dto.setSenderId(Integer.parseInt(entity.getChatSender()));

        String chatName = entity.getCareChatRoom().getChatName();
        String[] ids = chatName.split("-");
        Integer member1 = Integer.parseInt(ids[0]);
        Integer member2 = Integer.parseInt(ids[1]);

        // 보낸 사람 기준으로 받는 사람 계산
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
            } else {
                // 일반 채팅
                dto.setType("CHAT");
                dto.setContent(rawMessage);
            }
        } catch (Exception e) {
            // 파싱 실패 시 fallback
            dto.setType("CHAT");
            dto.setContent(entity.getChatMessage());
        }

        return dto;
    }
}
