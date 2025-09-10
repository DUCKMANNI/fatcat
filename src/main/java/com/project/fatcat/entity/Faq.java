package com.project.fatcat.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "faqs")
public class Faq {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer faqSeq;

	@Column(nullable = false)
	private String faqCode;

	@Column(nullable = false)
	private String question;

	@Column(nullable = false)
	private String answer;

	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private Integer viewCount;

	@Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;

	private LocalDateTime updateDate;

}