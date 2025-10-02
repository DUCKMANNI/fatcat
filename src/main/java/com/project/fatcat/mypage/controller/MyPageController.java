
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
import com.project.fatcat.entity.User;
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

    // 마이페이지 URL로 접속하면 이 메소드가 호출됩니다.
//    @GetMapping("/mypage")
//    // Principal 객체를 메소드 인자로 받아 현재 로그인된 사용자의 ID(Principal Name)를 가져옵니다.
//    public String myPage(Model model) {
//
//    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth == null || !auth.isAuthenticated()) {
//            throw new IllegalStateException("로그인 필요");
//        }
//    	
//    	Object principal = auth.getPrincipal();
//        Integer userSeq; 
//        
//        if (principal instanceof CustomUserDetails userDetails) {
//        	userSeq = userDetails.getUser().getUserSeq();
//        } else {
//            throw new IllegalStateException("인증된 사용자 정보 없음");
//        }
//    	
//    	
//    	 String userEmail = userDetails.getUser().getUserEmail();
//    	    Optional<User> userOptional = userRepository.findByUserEmail(userEmail);
//    	    User user = userOptional.orElseThrow(() -> 
//    	        new RuntimeException("Authenticated user [" + userEmail + "] not found in database."));
//    	    System.out.println("------------------------------------------------------------------------------------------------------------");
//    	    System.out.println("auth.isAuthenticated() : " + auth.isAuthenticated() );
//
//    	    // 3. 사용자 정보 및 고양이 정보 Model에 추가 (기존 코드)
//    	    List<Cat> cats = catService.findAllByUserId(userSeq);
//    	    model.addAttribute("cats", cats);
//    	    model.addAttribute("user", user);
//
//    	    // 4. [핵심 추가] 주문 내역 Model에 추가
//    	    // 새로 만든 OrderService를 호출하여 주문 목록을 가져옵니다.
//    	    List<Order> orderList = orderService.getOrdersByUserId(userSeq); 
//    	    model.addAttribute("orderList", orderList); // 👈 "orderList" 이름으로 뷰에 전달!
//    	    model.addAttribute("isAuthenticated", auth != null && auth.isAuthenticated());
//
//    	    return "mypage/mypage";
//    	}
    	
    
    
    
    
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

