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
@Table(name = "inquiry_comments")
public class InquiryComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inquiryCommentSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "inquiry_seq", nullable = true)
	private Inquiry inquiry;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = true)
	private User user;
	
	@Column(nullable = false)
	private String inquiryComment;
	
	@Column( columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@Column(columnDefinition = "BOOLEAN DEFAULT 0")
	private Boolean isDeleted;

}