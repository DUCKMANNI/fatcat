package com.project.fatcat.entity;



import jakarta.persistence.*;

import jakarta.persistence.CascadeType;

import java.util.*;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer couponSeq;
    
    @Column( nullable = false)
    private String couponName;
    
    @Column(nullable = false)
    private Integer couponDiscount;
    
    @Column(nullable = false)
    private String couponLimitdate;
    
    @Builder.Default
    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL)
    private List<UserCoupon> userCouponList = new ArrayList<>();


}