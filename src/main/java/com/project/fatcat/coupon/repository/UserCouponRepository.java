package com.project.fatcat.coupon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.UserCoupon;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Integer>{

	List<UserCoupon> findByUser_UserSeq(Integer userSeq);
	
	@Query("SELECT uc FROM UserCoupon uc " +
	           "WHERE uc.user.userSeq = :userSeq " +
	           "AND uc.isUsed = false " +
	           "AND uc.coupon.couponLimitdate >= CURRENT_DATE")
	    List<UserCoupon> findAvailableCoupons(@Param("userSeq") Integer userSeq);
}
