package com.project.fatcat.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.CareChatHistory;
import com.project.fatcat.entity.CareChatRoom;

public interface CareChatHistoryRepository extends JpaRepository<CareChatHistory, Integer>{

	List<CareChatHistory> findByCareChatRoomOrderByChatDateAsc(CareChatRoom careChatRoom);
}
