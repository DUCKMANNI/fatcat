package com.project.fatcat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "products")
public class Product {

	@Id
	@Column( length = 20)
	private String productCode; // 최종코드 (예: 10A001)

	@Column(nullable = false)
	private String productName;

	private String productContent;

	@Column(nullable = false)
	private Integer productPrice;

	@Column(nullable = false)
	private Integer productStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_seq", nullable = false)
    private Category category;

	@Column(nullable = false)
	private Integer productSeq; // 일련번호 (001, 002 …)

	@Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
	private LocalDateTime createDate;

	private LocalDateTime updateDate;

	@Builder.Default
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<ProductImage> productImageList = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<CartItem> cartItemList = new ArrayList<>();

	@Builder.Default
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	private List<OrderItem> orderItemList = new ArrayList<>();


}