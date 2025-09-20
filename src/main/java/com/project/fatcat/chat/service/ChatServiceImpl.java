package com.project.fatcat.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.fatcat.chat.dto.ChatMessageDto;
import com.project.fatcat.chat.repository.CareChatHistoryRepository;
import com.project.fatcat.chat.repository.ChatRoomRepository;
import com.project.fatcat.entity.CareChatHistory;
import com.project.fatcat.entity.CareChatRoom;
import com.project.fatcat.entity.User;
import com.project.fatcat.users.UserRepository;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final CareChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChatServiceImpl(ChatRoomRepository chatRoomRepository, CareChatHistoryRepository chatHistoryRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatHistoryRepository = chatHistoryRepository;
        this.userRepository = userRepository;
    }

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
            // 엔티티 리스트를 DTO 리스트로 변환하여 반환
            return history.stream().map(this::convertToDto).collect(Collectors.toList());
        }
        return List.of();
    }

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto chatMessageDto) {
        // DTO를 엔티티로 변환
        CareChatHistory chatHistory = convertToEntity(chatMessageDto);
        chatHistory.setChatDate(LocalDateTime.now());

        // 엔티티를 저장하고, 저장된 엔티티를 다시 DTO로 변환하여 반환
        CareChatHistory savedHistory = chatHistoryRepository.save(chatHistory);
        return convertToDto(savedHistory);
    }
    
    // --- DTO와 엔티티 간 변환 메서드 ---
    private ChatMessageDto convertToDto(CareChatHistory entity) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setChatRoomId(entity.getCareChatRoom().getChatSeq());
        dto.setSenderId(entity.getUser().getUserSeq());
        dto.setContent(entity.getChatMessage());
        dto.setTimestamp(entity.getChatDate());
        return dto;
    }

    private CareChatHistory convertToEntity(ChatMessageDto dto) {
        CareChatHistory entity = new CareChatHistory();
        
        // DTO에서 받은 ID를 사용하여 엔티티를 조회하여 연결
        CareChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow();
        User senderUser = userRepository.findById(dto.getSenderId()).orElseThrow();
        
        entity.setCareChatRoom(chatRoom);
        entity.setUser(senderUser);
        entity.setChatSender(String.valueOf(dto.getSenderId()));
        entity.setChatMessage(dto.getContent());
        // chatDate는 서비스에서 설정
        
        return entity;
    }
}