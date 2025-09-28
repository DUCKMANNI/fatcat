package com.project.fatcat.shopping.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.project.fatcat.users.service.CustomUserDetails;

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
    
    private ShoppingCart getOrCreateOpenCart() {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("로그인 필요");
        }

        Object principal = auth.getPrincipal();
        Integer userSeq; 
        
        if (principal instanceof CustomUserDetails userDetails) {
        	userSeq = userDetails.getUser().getUserSeq();
        } else {
            throw new IllegalStateException("인증된 사용자 정보 없음");
        }

        User user = new User();
        user.setUserSeq(userSeq);
    	
        return shoppingCartRepository.findFirstByUser_UserSeqAndIsCompletedFalseOrderByCartSeqDesc(userSeq)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart();
                    cart.setUser(user); 
                    cart.setIsCompleted(false);
                    return shoppingCartRepository.save(cart);
                });
    }
  
    @Override
    public void add(String productCode, int qty) {
    	
        ShoppingCart cart = getOrCreateOpenCart();
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
    public void updateQty(String productCode, int qty) {
        ShoppingCart cart = getOrCreateOpenCart();
        cart.getCartItemList().stream()
                .filter(ci -> ci.getProduct().getProductCode().equals(productCode))
                .findFirst()
                .ifPresent(ci -> ci.setProductQuantity(Math.max(1, Math.min(qty, 99))));
    }

    @Override
    public void remove(String productCode) {
        ShoppingCart cart = getOrCreateOpenCart();
        cart.getCartItemList().removeIf(ci -> ci.getProduct().getProductCode().equals(productCode));
        // orphanRemoval=true 라면 DB에서 자동 삭제
    }

    @Override
    public void clear() {
        ShoppingCart cart = getOrCreateOpenCart();
        cart.getCartItemList().clear();
    }

    
    @Override
    public List<CartItemDTO> getCartItems() {
        ShoppingCart cart = getOrCreateOpenCart();
        return cart.getCartItemList().stream()
                .map(ci -> CartItemDTO.builder()
                        .productName(ci.getProduct().getProductName())
                        .quantity(ci.getProductQuantity())
                        .price(ci.getProduct().getProductPrice() * ci.getProductQuantity())
                        .imageUrl(
                            ci.getProduct().getProductImageList().stream()
                              .filter(img -> "m".equals(img.getImageTypeCode()))
                              .map(ProductImage::getFileName)
                              .findFirst().orElse("/images/no-image.png")
                        )
                        .build())
                .toList();
    }

    @Override
    public CartSummaryDTO summarize() {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("로그인 필요");
        }
    	
    	Object principal = auth.getPrincipal();
        Integer userSeq; 
        
        if (principal instanceof CustomUserDetails userDetails) {
        	userSeq = userDetails.getUser().getUserSeq();
        } else {
            throw new IllegalStateException("인증된 사용자 정보 없음");
        }
    	
        List<CartItemDTO> items = getCartItems();
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
    
    public ShoppingCart getCurrentCart() {
        return getOrCreateOpenCart();
    }
}
