package com.project.fatcat.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.OrderItem;
import com.project.fatcat.entity.Product;
import com.project.fatcat.entity.User;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

	boolean existsByOrderUserAndProduct(User user, Product product);
}
