package com.project.fatcat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "inquiry")
public class Inquiry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer inquirySeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = true)
	private User user;
	
	@Column(nullable = false)
	private String inquiryTitle;
	
	@Column(nullable = false)
	private String inquiryContent;
	
	@Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@Column(nullable = false, columnDefinition = "INT DEFAULT 0")
	private Integer viewCount;
	
	@Column(columnDefinition = "BOOLEAN DEFAULT 0")
	private Boolean isDeleted;
	
	@Builder.Default
	@OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
	private List<InquiryComment> inquiryCommentList = new ArrayList<>();

}