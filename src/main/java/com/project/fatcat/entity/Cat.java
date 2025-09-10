package com.project.fatcat.entity;

import java.time.LocalDate;
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
@Table(name = "cats")
public class Cat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer catSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = true)
	private User user;
	
	@Column( nullable = false)
	private String catName;
	
	private LocalDate catBirtthday;
	
	private String catImageUrl;
	
	@Column( columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;


}