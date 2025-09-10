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
@Table(name = "vet_reviews")
public class VetReview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vetReviewSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vet_seq", nullable = true)
	private VetClinic vetClinic;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = true)
	private User user;
	
	@Column( nullable = false)
	private String vetReview;
	
	@Column(nullable = false)
	private Integer vetRating;
	
	@Column(nullable = false)
	private LocalDateTime visitDate;
	
	@Column(columnDefinition = "INT DEFAULT 0")
	private Integer helpfulCount;
	
	@Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;
	
	private LocalDateTime updateDate;
	
	@Builder.Default
	@OneToMany(mappedBy = "vetReview", cascade = CascadeType.ALL)
	private List<VetReviewLike> vetReviewLikeList = new ArrayList<>();

}