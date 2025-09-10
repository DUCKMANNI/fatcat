package com.project.fatcat.entity;



import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.project.fatcat.entity.enums.CareBoardStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "care_service_boards")
public class CareServiceBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer careSeq;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;
    
    @Column(nullable = false)
    private String careTitle;
    
    private String careContent;
    
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer viewCount;
    
    @Enumerated(EnumType.STRING)
    private CareBoardStatus status = CareBoardStatus.OPEN;
    
    @Column( nullable = false)
    private String address1;
    
    @Column( nullable = false)
    private String address2;
    
    @Column(nullable = false)
    private Double latitude;
    
    @Column(nullable = false)
    private Double longitude;
    
    @Column(nullable = false)
    private Integer price;
    
    @Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT 0")
    private Boolean isDeleted;
    
    @OneToMany(mappedBy = "careServiceBoard", cascade = CascadeType.ALL)
    private List<CareSession> careSessionList = new ArrayList<>();


}