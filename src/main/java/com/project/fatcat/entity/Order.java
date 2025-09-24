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
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private User user;
	
	private String orderNumber;
	
	@Column(nullable = false)
	private Integer orderPrice;
	
	@Column(nullable = false)
	private String orderName;
	
	@Column(nullable = false)
	private String orderPhonenum;
	
	@Column(nullable = false)
	private String orderAddress1;
	
	@Column( nullable = false)
	private String orderAddress2;
	
	@Column(nullable = false)
	private Double latitude;
	
	@Column(nullable = false)
	private Double longitude;
	
	@Column(nullable = false)
	private String orderZipcode;
	
	private String orderRequest;
	
	@Column( nullable = false, columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime orderDate;
	
	@Column(nullable = false)
	private String orderStatus;
	
	@Builder.Default
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<Payment> paymentList = new ArrayList<>();
	
	@Builder.Default
	@OneToMany(mappedBy = "orderInfo", cascade = CascadeType.ALL)
	private List<OrderItem> orderItemList = new ArrayList<>();

}