package com.project.fatcat.shopping.service;

import java.util.List;

import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.shopping.dto.CartItemDTO;
import com.project.fatcat.shopping.dto.CartSummaryDTO;

public interface CartService {
	
	ShoppingCart findById(Integer cartSeq);
	void add(Integer userSeq, String productCode, int qty);
    void updateQty(Integer userSeq, String productCode, int qty);
    void remove(Integer userSeq, String productCode);
    void clear(Integer userSeq);
    CartSummaryDTO summarize(Integer userSeq);
    List<CartItemDTO> getCartItems(Integer userSeq);
    
}
