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
import com.project.fatcat.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final CareChatHistoryRepository chatHistoryRepository;
    private final UserRepository userRepository;



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

    // --- DTO와 엔티티 변환 ---

    private CareChatHistory convertToEntity(ChatMessageDto dto) {
        CareChatHistory entity = new CareChatHistory();
        CareChatRoom chatRoom = chatRoomRepository.findById(dto.getChatRoomId()).orElseThrow();
        
        // [수정] Integer 타입의 senderId를 String으로 변환하여 설정
        entity.setChatSender(String.valueOf(dto.getSenderId())); 
        
        entity.setCareChatRoom(chatRoom);
        entity.setChatMessage(dto.getContent());
        
        return entity;
    }

    private ChatMessageDto convertToDto(CareChatHistory entity) {
        ChatMessageDto dto = new ChatMessageDto();
        
        dto.setChatRoomId(entity.getCareChatRoom().getChatSeq());
        dto.setSenderId(Integer.parseInt(entity.getChatSender()));

        String chatName = entity.getCareChatRoom().getChatName();
        String[] ids = chatName.split("-");
        
        Integer member1 = Integer.parseInt(ids[0]);
        Integer member2 = Integer.parseInt(ids[1]);
        
        if (Integer.parseInt(entity.getChatSender()) == member1) {
            dto.setReceiverId(member2);
        } else {
            dto.setReceiverId(member2);
        }

        dto.setContent(entity.getChatMessage());  
        dto.setTimestamp(entity.getChatDate());
        
        return dto;
    }
}