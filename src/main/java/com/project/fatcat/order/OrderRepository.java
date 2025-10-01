package com.project.fatcat.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fatcat.entity.Order;

@Repository("myPageOrderRepository") 
public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	// Spring Data JPA 쿼리 메소드: User 엔티티의 userSeq(UserNo)로 주문 목록 조회
    // User 엔티티 안에 Order 리스트가 연결되어 있다면, 해당 필드명으로 조회합니다.
    List<Order> findByUserUserSeq(Integer userSeq); 
    // 혹은 Order 엔티티에 userId 필드가 있다면 findByUserId(Integer userId);

}
