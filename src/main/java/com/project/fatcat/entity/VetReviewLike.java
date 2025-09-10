package com.project.fatcat.entity;

import java.time.LocalDateTime;

import com.project.fatcat.entity.pk.VetReviewLikeId;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "vet_review_likes", uniqueConstraints = {@UniqueConstraint(name = "uq_vet_review_likes", columnNames = {"vet_review_seq", "user_seq"})})
public class VetReviewLike {

	@EmbeddedId
    private VetReviewLikeId id;   // PK: (vetReviewSeq, userSeq)

	@MapsId("vetReviewSeq")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vet_review_seq", nullable = false)
	private VetReview vetReview;

	@MapsId("userSeq") 
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = false)
	private User user;
	
	private LocalDateTime createDate;


}