
package com.project.fatcat.mypage.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.fatcat.SecurityUtils;
import com.project.fatcat.care.service.CareSessionService;
import com.project.fatcat.cats.service.CatService;
import com.project.fatcat.coupon.service.CouponService;
import com.project.fatcat.entity.Cat;
import com.project.fatcat.entity.Order;
import com.project.fatcat.entity.UserCoupon;
import com.project.fatcat.shopping.service.OrderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // 로깅을 위해 추가

@Controller
@RequiredArgsConstructor
@Slf4j // 로깅 사용
public class MyPageController {
    
    private final CatService catService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final CareSessionService careSessionService;

    
    @GetMapping("/mypage")
    public String myPage(Model model) {

        Integer userSeq = SecurityUtils.getCurrentUserSeq();
        //String userEmail = SecurityUtils.getCurrentUser().getUsername();

        
        List<UserCoupon> coupons = couponService.getUserCoupons(userSeq);
        model.addAttribute("coupons", coupons);

        // 고양이 정보
        List<Cat> cats = catService.findAllByUserId(userSeq);
        model.addAttribute("cats", cats);

        // 주문 내역
        List<Order> orderList = orderService.getOrdersByUserId(userSeq);
        model.addAttribute("orderList", orderList);
        
        model.addAttribute("ownerSessions", careSessionService.getOwnerSessions(SecurityUtils.getCurrentUser().getUser()));
        model.addAttribute("sitterSessions", careSessionService.getSitterSessions(SecurityUtils.getCurrentUser().getUser()));

        // 사용자 정보 + 인증 상태
        model.addAttribute("user", SecurityUtils.getCurrentUser());
        model.addAttribute("isAuthenticated", true);

        return "mypage/mypage";
    }
    
}

