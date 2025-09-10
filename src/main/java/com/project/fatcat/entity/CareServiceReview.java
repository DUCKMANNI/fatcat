package com.project.fatcat.entity;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "care_service_reviews")
public class CareServiceReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer careReviewSeq;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_seq", nullable = true)
    private CareSession careSession;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_user_seq", nullable = true)
    private User authorUser;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_user_seq", nullable = true)
    private User targetUser;
    
    @Column( nullable = false)
    private String targetRole;
    
    @Column(nullable = false)
    private Integer careRating;
    
    @Column(nullable = false)
    private String careReview;
    
    @Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isDeleted;


}