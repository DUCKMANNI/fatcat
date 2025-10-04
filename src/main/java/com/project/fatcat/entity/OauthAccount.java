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
@Table(name = "oauth_accounts", uniqueConstraints = {@UniqueConstraint(name = "uq_provider_identity", columnNames = {"provider", "provider_user_id"})})
public class OauthAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer oauthSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq", nullable = true)
	private User user;
	
	private String providerEmail;
	
	@Column(nullable = false)
	private String provider;
	
	@Column( nullable = false)
	private String providerUserId;
	
	@Column(insertable = false, updatable = false,
	        columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime connectedDate;

}