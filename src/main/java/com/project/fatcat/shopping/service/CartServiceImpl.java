package com.project.fatcat.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.fatcat.entity.CartItem;
import com.project.fatcat.entity.Product;
import com.project.fatcat.entity.ProductImage;
import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.entity.User;
import com.project.fatcat.shopping.dto.CartItemDTO;
import com.project.fatcat.shopping.dto.CartSummaryDTO;
import com.project.fatcat.shopping.repository.ProductRepository;
import com.project.fatcat.shopping.repository.ShoppingCartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService{

	private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
   
    @Override
    public ShoppingCart findById(Integer cartSeq) {
        return shoppingCartRepository.findById(cartSeq)
                .orElseThrow(() -> new RuntimeException("장바구니 없음"));
    }
    
    private ShoppingCart getOrCreateOpenCart(Integer userSeq) {
    	
    	User user = new User();
        user.setUserSeq(1);   
    	
        return shoppingCartRepository.findFirstByUser_UserSeqAndIsCompletedFalseOrderByCartSeqDesc(userSeq)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user); 
                    cart.setIsCompleted(false);
                    return shoppingCartRepository.save(cart);
                });
    }
  
    @Override
    public void add(Integer userSeq, String productCode, int qty) {
        ShoppingCart cart = getOrCreateOpenCart(userSeq);
        Product product = productRepository.findById(productCode)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productCode));
        int q = Math.max(1, Math.min(qty, 99));
        System.out.println(qty);
        System.out.println(q);

        // 같은 상품 이미 있으면 수량 증가
        CartItem exist = cart.getCartItemList().stream()
                .filter(ci -> ci.getProduct().getProductCode().equals(productCode))
                .findFirst().orElse(null);

        if (exist == null) {
            CartItem item = CartItem.builder()
                    .shoppingCart(cart)
                    .product(product)
                    .productQuantity(q)
                    .build();
            cart.getCartItemList().add(item);
        } else {
            exist.setProductQuantity(Math.max(1, Math.min(exist.getProductQuantity() + q, 99)));
        }

        shoppingCartRepository.save(cart);
    }
    
    @Override
    public void updateQty(Integer userSeq, String productCode, int qty) {
        ShoppingCart cart = getOrCreateOpenCart(userSeq);
        cart.getCartItemList().stream()
                .filter(ci -> ci.getProduct().getProductCode().equals(productCode))
                .findFirst()
                .ifPresent(ci -> ci.setProductQuantity(Math.max(1, Math.min(qty, 99))));
    }

    @Override
    public void remove(Integer userSeq, String productCode) {
        ShoppingCart cart = getOrCreateOpenCart(userSeq);
        cart.getCartItemList().removeIf(ci -> ci.getProduct().getProductCode().equals(productCode));
        // orphanRemoval=true 라면 DB에서 자동 삭제
    }

    @Override
    public void clear(Integer userSeq) {
        ShoppingCart cart = getOrCreateOpenCart(userSeq);
        cart.getCartItemList().clear();
    }

   // @Transactional(readOnly = true)
//    @Override
//    public CartSummaryDTO summarize(Integer userSeq) {
//    	
//    	User user = new User();
//    	user.setUserSeq(1);
//    	
//        ShoppingCart cart = shoppingCartRepository.findFirstByUser_UserSeqAndIsCompletedFalseOrderByCartSeqDesc(userSeq)
//                .orElseGet(() -> {
//                    // 비어있는 요약 반환
//                    return ShoppingCart.builder()
//                    		.user(user)
//                            .isCompleted(false)
//                            .cartItemList(new ArrayList<>())
//                            .build();
//                });
//        
//        
//
//        List<CartItemDTO> items = cart.getCartItemList().stream()
//        	    .map(ci -> {
//        	        CartItemDTO dto = new CartItemDTO();
//        	        dto.setProductName(ci.getProduct().getProductName());
//        	        dto.setPrice(ci.getProduct().getProductPrice());
//        	        dto.setQuantity(ci.getProductQuantity());
//        	        String imageUrl = ci.getProduct().getProductImageList().stream()
//        	                .filter(img -> "m".equals(img.getImageTypeCode())) 
//        	                .findFirst()
//        	                .map(ProductImage::getFileName)
//        	                .orElse("/images/no-image.png"); // 없으면 기본 이미지
//        	        dto.setImageUrl(imageUrl); // 이미지 경로 필드 있으면 매핑
//        	        return dto;
//        	    })
//        	    .toList();
//
//
//        int total = items.stream()
//                .mapToInt(dto -> dto.getPrice() * dto.getQuantity())
//                .sum();
//
////        int totalQty = items.stream()
////                .mapToInt(CartItemDTO::getQuantity)
////                .sum();
//        
//        CartSummaryDTO summary = new CartSummaryDTO();
//        summary.setTotalPrice(total);
//        summary.setDiscount(0);
//        summary.setDeliveryFee(3000);
//        summary.setFinalPrice(total - summary.getDiscount() + summary.getDeliveryFee());
//
//        return summary;
//    }
    
    @Override
    public List<CartItemDTO> getCartItems(Integer userSeq) {
        ShoppingCart cart = shoppingCartRepository
            .findFirstByUser_UserSeqAndIsCompletedFalseOrderByCartSeqDesc(userSeq)
            .orElseThrow(() -> new RuntimeException("장바구니 없음"));

        return cart.getCartItemList().stream()
                .map(ci -> CartItemDTO.builder()
                        .productName(ci.getProduct().getProductName())
                        .quantity(ci.getProductQuantity())
                        .price(ci.getProduct().getProductPrice() * ci.getProductQuantity())
                        .imageUrl(
                            ci.getProduct().getProductImageList().stream()
                              .filter(img -> "m".equals(img.getImageTypeCode())) // 대표 이미지
                              .map(ProductImage::getFileName)
                              .findFirst().orElse("/images/no-image.png")
                        )
                        .build())
                .toList();
    }

    @Override
    public CartSummaryDTO summarize(Integer userSeq) {
        List<CartItemDTO> items = getCartItems(userSeq);
        int totalPrice = items.stream().mapToInt(CartItemDTO::getPrice).sum();
        int discount = 0; // 쿠폰/할인 아직 없음
        int deliveryFee = totalPrice >= 30000 ? 0 : 3000; // 3만원 이상 무료배송 예시
        int finalPrice = totalPrice - discount + deliveryFee;

        return CartSummaryDTO.builder()
                .totalPrice(totalPrice)
                .discount(discount)
                .deliveryFee(deliveryFee)
                .finalPrice(finalPrice)
                .build();
    }
}
