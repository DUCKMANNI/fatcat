package com.project.fatcat.shopping.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.fatcat.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

	Optional<Order> findByOrderNumber(String orderNum);
	
	List<Order> findByUserUserSeq(Integer userSeq); 
	
	 @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItemList oi JOIN FETCH oi.product p WHERE o.user.userSeq = :userSeq ORDER BY o.orderDate DESC")
	    List<Order> findOrdersWithDetailsByUserSeq(@Param("userSeq") Integer userSeq);
}
