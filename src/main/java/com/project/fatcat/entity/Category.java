package com.project.fatcat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categorySeq;

    @Column(nullable = false, length = 50)
    private String mainCategory;   // 대분류 (사료, 간식, ...)

    @Column(nullable = false, length = 10)
    private String mainCode;       // 대분류 코드 (예: 10, 11 ...)

    @Column(length = 50)
    private String subCategory;    // 중분류 (건식, 습식 ...)

    @Column(length = 10)
    private String subCode;        // 중분류 코드 (예: A, B ...)

    @Column(length = 50)
    private String detailCategory; // 소분류 (노묘, 성묘, 키튼 ...)

}
