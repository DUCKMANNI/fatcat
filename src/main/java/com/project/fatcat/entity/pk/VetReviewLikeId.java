package com.project.fatcat.entity.pk;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class VetReviewLikeId implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private Integer vetReviewSeq;

	private Integer userSeq;
}
