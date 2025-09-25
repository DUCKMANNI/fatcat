package com.project.fatcat.shopping.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.shopping.dto.CartItemDTO;
import com.project.fatcat.shopping.dto.CartSummaryDTO;
import com.project.fatcat.shopping.dto.OrderDTO;
import com.project.fatcat.shopping.dto.OrderFormDTO;
import com.project.fatcat.shopping.dto.PaymentConfirmResponse;
import com.project.fatcat.shopping.service.CartServiceImpl;
import com.project.fatcat.shopping.service.OrderService;
import com.project.fatcat.shopping.service.TossPaymentServiceImpl;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartServiceImpl cartServiceImpl; 
    private final TossPaymentServiceImpl tossPaymentServiceImpl;

    /** 주문서 작성 화면 */
    @GetMapping("/form")
    public String orderForm(@RequestParam("cartSeq") Integer cartSeq, Model model) {
        ShoppingCart cart = cartServiceImpl.findById(cartSeq);

        // CartItem → DTO 변환
        List<CartItemDTO> items = cart.getCartItemList().stream()
        		.map(ci -> CartItemDTO.builder()
        				.productName(ci.getProduct().getProductName())
        				.price(ci.getProduct().getProductPrice())
        				.quantity(ci.getProductQuantity())
        				.imageUrl(
        						ci.getProduct().getProductImageList().stream()
        						.filter(img -> "m".equals(img.getImageTypeCode()))
        						.findFirst()
        						.map(img -> img.getFileName())
        						.orElse(null)
                              )
                              .build())
                      .collect(Collectors.toList());

        // OrderDTO (출력용)
        OrderDTO orderDTO = OrderDTO.builder()
                .cartSeq(cartSeq)
                .userName(cart.getUser().getUserName())
                .userEmail(cart.getUser().getUserEmail())
                .userPhone(cart.getUser().getPhoneNumber())
                .orderName(cart.getUser().getUserName())
                .orderPhonenum(cart.getUser().getPhoneNumber())
                .orderAddress1(cart.getUser().getAddress1())
                .orderAddress2(cart.getUser().getAddress2())
                .orderZipcode(cart.getUser().getZipCode())
                .latitude(20.0)
                .longitude(30.0)
                .items(items)
                .build();

        // 요약 정보
        int totalPrice = items.stream().mapToInt(i -> i.getPrice() * i.getQuantity()).sum();
        int discount = 0;
        int deliveryFee = totalPrice >= 50000 ? 0 : 3000;
        int finalPrice = totalPrice - discount + deliveryFee;

        CartSummaryDTO summary = CartSummaryDTO.builder()
                .totalPrice(totalPrice)
                .discount(discount)
                .deliveryFee(deliveryFee)
                .finalPrice(finalPrice)
                .build();

        // OrderFormDTO (입력용)
        OrderFormDTO orderForm = OrderFormDTO.builder()
                .cartSeq(cartSeq)
                .build();

        model.addAttribute("orderDTO", orderDTO);
        model.addAttribute("orderForm", orderForm);
        model.addAttribute("cartSummary", summary);

        return "order/order_form";
    }

    /** 주문 확정 + 결제 화면 이동 */
    @PostMapping("/confirm")
    public String orderConfirm(@ModelAttribute("orderForm") OrderFormDTO orderForm, Model model) {
        // 주문 저장 (orderNumber 생성 포함)
        var order = orderService.confirmOrder(orderForm, orderForm.getCartSeq());

        // 요약 계산
        ShoppingCart cart = cartServiceImpl.findById(orderForm.getCartSeq());
        int totalPrice = cart.getCartItemList().stream()
                .mapToInt(ci -> ci.getProduct().getProductPrice() * ci.getProductQuantity())
                .sum();
        int discount = 0;
        int deliveryFee = totalPrice >= 50000 ? 0 : 3000;
        int finalPrice = totalPrice - discount + deliveryFee;

        CartSummaryDTO summary = CartSummaryDTO.builder()
                .totalPrice(totalPrice)
                .discount(discount)
                .deliveryFee(deliveryFee)
                .finalPrice(finalPrice)
                .build();

        // DB에 저장된 orderNumber 사용
        orderForm.setOrderNumber(order.getOrderNumber());
        
        System.out.println("getReceiverName222"+orderForm.getReceiverName());
        System.out.println("orderForm"+orderForm.getCartSeq());
        

        model.addAttribute("orderForm", orderForm);
        model.addAttribute("cartSummary", summary);
        model.addAttribute("clientKey", "test_ck_5OWRapdA8dqYpD5mNXX93o1zEqZK");

        return "order/payment";
    }

    
   
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentKey") String paymentKey,
                                 @RequestParam("orderId") String orderNumber,
                                 @RequestParam("amount") int amount,
                                 @RequestParam("cartSeq") Integer cartSeq,
                                 Model model) {

        PaymentConfirmResponse response = tossPaymentServiceImpl.confirmPayment(paymentKey, orderNumber, amount, cartSeq);
        model.addAttribute("payment", response);

        return "order/success";
    }

    @GetMapping("/fail")
    public String paymentFail(@RequestParam("code") String code,
                              @RequestParam("message") String message,
                              Model model) {
        model.addAttribute("errorCode", code);
        model.addAttribute("errorMessage", message);
        return "order/fail";
    }
}
