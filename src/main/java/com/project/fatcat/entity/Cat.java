package com.project.fatcat.entity;

import java.time.LocalDate;
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
import jakarta.validation.constraints.NotBlank; // @NotBlank 어노테이션을 위해 추가했습니다.
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Enum 타입 사용을 위한 import
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cats")
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer catSeq;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = true)
    private User user;
    
    // 이름 (필수)
    @Column(nullable = false)
    private String catName;
    
    // 생일
    private LocalDate catBirtthday;
    
    private String catImageUrl;
    
    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;

    // --- 추가된 필드 ---
    
    // 성별(MALE, FEMALE)을 위한 Enum (필수)
    public enum Gender {
        MALE, FEMALE
    }
    
    // Enum 값을 문자열로 저장하기 위한 어노테이션
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender catGender;
    
    // 종 이름 (필수, 빈 값이나 공백만 있는 값도 허용하지 않음)
    @NotBlank
    @Column(nullable = false)
    private String catBreed;
    
    // 중성화 여부 (필수)
    @Column(nullable = false)
    private boolean isNeutered;
    
    // 질병 여부
    private boolean hasDisease;
    
    // 알레르기 여부
    private boolean hasAllergy;

}
