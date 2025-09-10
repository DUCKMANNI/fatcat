package com.project.fatcat.entity;

import java.math.BigDecimal;
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
@Table(name = "vet_rating_avg")
public class VetRatingAvg {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer vetRatingSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vet_seq")
	private VetClinic vetClinic;
	
	@Column(nullable = false)
	private Integer ratingCount;
	
	@Column(nullable = false)
	private Integer ratingSum;
	
	@Column(nullable = false)
	private BigDecimal ratingAvg;
	
	private LocalDateTime lastReviewDate;

}