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
@Table(name = "payments")
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer paySeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_seq")
	private Order order;
	
	@Column(nullable = false)
	private Integer payPrice;	//totalAmount -> 결제금
	
	@Column(nullable = false)
	private String merchantUid;	//orderId -> 토스 시스템 주문번호
	
	@Column( nullable = false)
	private String pgProvider;	//provider or mId -> 결제 PG사 (토스페이먼츠 등)
	
	@Column(nullable = false)
	private String method;		//method -> 결제 방식 (CARD, ACCOUNT 등)
	
	@Column(nullable = false)
	private String status;		//status -> DONE, CANCELED, FAILED
	
	@Column(nullable = false)
	private String pgTid;		//transactionKey or paymentKey -> PG사 거래 고유번호
	
	@Column(insertable = false, updatable = false,
	        columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime approvedDate;		//approvedAt
	
	private LocalDateTime failedDate;
	
	private LocalDateTime canceledDate;
	
}

