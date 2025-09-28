package com.project.fatcat.shopping.service;

import java.util.List;

import com.project.fatcat.entity.ShoppingCart;
import com.project.fatcat.shopping.dto.CartItemDTO;
import com.project.fatcat.shopping.dto.CartSummaryDTO;

public interface CartService {
	
	ShoppingCart findById(Integer cartSeq);
	void add(String productCode, int qty);
    void updateQty(String productCode, int qty);
    void remove(String productCode);
    void clear();
    CartSummaryDTO summarize();
    List<CartItemDTO> getCartItems();
    
}
