package com.project.fatcat.inquiry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.fatcat.entity.Inquiry;
import com.project.fatcat.entity.InquiryComment;


@Repository
// JpaRepository의 제네릭을 InquiryComment 엔티티로 정확하게 지정했습니다.
public interface InquiryCommentRepository extends JpaRepository<InquiryComment, Integer> {
	
	 /**
     * 특정 Inquiry에 속하며 삭제되지 않은 댓글/답변 목록을 조회합니다.
     */
    List<InquiryComment> findByInquiryAndIsDeletedFalse(Inquiry inquiry);

}
