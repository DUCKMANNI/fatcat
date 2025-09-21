package com.project.fatcat.vet.repository;

import com.project.fatcat.entity.VetReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VetReviewRepository extends JpaRepository<VetReview, Integer> {
    
    // 이 메서드는 VetClinic 엔티티의 vetSeq를 기준으로 리뷰를 찾습니다.
  
    List<VetReview> findByVetClinicVetSeq(Integer vetSeq);
    
   
}