package com.project.fatcat.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.fatcat.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{

}
