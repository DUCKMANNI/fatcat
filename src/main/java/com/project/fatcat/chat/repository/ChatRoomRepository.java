package com.project.fatcat.chat.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.CareChatRoom;

public interface ChatRoomRepository extends JpaRepository<CareChatRoom, Integer> {

	Optional<CareChatRoom> findByChatName(String chatName);
}
