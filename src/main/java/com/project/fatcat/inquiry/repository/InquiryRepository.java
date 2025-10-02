package com.project.fatcat.inquiry.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fatcat.entity.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Integer> {
	
	  // InquiryService에서 사용됨: isDeleted=FALSE 인 문의를 페이징하여 조회
    Page<Inquiry> findByIsDeletedFalse(Pageable pageable);

    // InquiryService에서 사용됨: 제목 또는 내용으로 검색
    Page<Inquiry> findByInquiryTitleContainingOrInquiryContentContaining(String title, String content, Pageable pageable);
}
