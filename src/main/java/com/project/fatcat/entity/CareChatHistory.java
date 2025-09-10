package com.project.fatcat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "care_chat_historys")
public class CareChatHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer chatHistorySeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_seq", nullable = true)
	private CareChatRoom careChatRoom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = true)
	private User user;
	
	@Column(nullable = false)
	private String chatMessage;
	
	@Column(nullable = false)
	private LocalDateTime chatDate;
	
	@Column(nullable = false)
	private String chatSender;

}