package com.project.fatcat.shopping.service;

import com.project.fatcat.entity.Order;
import com.project.fatcat.entity.Payment;
import com.project.fatcat.entity.User;

public interface PaymentService {

	Payment confirmAndSave(String paymentKey, String orderId, int amount, User user, Order order);
}
