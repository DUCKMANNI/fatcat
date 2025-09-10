package com.project.fatcat.entity;

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
@Table(name = "cart_items", uniqueConstraints = {@UniqueConstraint(name = "uq_cart_items", columnNames = {"cart_seq", "product_code"})})
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cartItemSeq;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_seq")
	private ShoppingCart shoppingCart;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_code")
	private Product product;
	
	@Column(nullable = false, columnDefinition="INT DEFAULT 1")
	private Integer productQuantity;


}