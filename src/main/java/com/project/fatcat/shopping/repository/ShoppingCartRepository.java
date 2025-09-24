package com.project.fatcat.shopping.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.ShoppingCart;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer>{

	Optional<ShoppingCart> findFirstByUser_UserSeqAndIsCompletedFalseOrderByCartSeqDesc(Integer userSeq);

}
