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
@Table(name = "order_items", uniqueConstraints = {@UniqueConstraint(name = "uq_order_items", columnNames = {"order_seq", "product_code"})})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemSeq;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_seq", nullable = true)
    private Order orderInfo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_code", nullable = true)
    private Product product;
    
    @Column(nullable = false)
    private Integer productQuantity;
    
    @Column(nullable = false)
    private Integer productPrice;
    
    @Column(nullable = false)
    private Integer discountTotal;
    
    @Column(nullable = false)
    private Integer totalPrice;


}