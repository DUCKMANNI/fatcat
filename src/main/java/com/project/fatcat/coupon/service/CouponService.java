package com.project.fatcat.coupon.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.fatcat.coupon.repository.UserCouponRepository;
import com.project.fatcat.entity.UserCoupon;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final UserCouponRepository userCouponRepository;
	
	// 마이페이지 → 내가 가진 전체 쿠폰
    public List<UserCoupon> getUserCoupons(Integer userSeq) {
        return userCouponRepository.findByUser_UserSeq(userSeq);
    }

    // 결제 페이지 → 사용 가능한 쿠폰만
    public List<UserCoupon> getAvailableCoupons(Integer userSeq) {
        return userCouponRepository.findAvailableCoupons(userSeq);
    }
}
