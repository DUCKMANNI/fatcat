package com.project.fatcat.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	Optional<Order> findByOrderNumber(String orderNum);
	
	List<Order> findByUserUserSeq(Integer userSeq); 
	
}
