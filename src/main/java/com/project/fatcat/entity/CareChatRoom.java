package com.project.fatcat.entity;



import java.time.LocalDateTime;

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



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "care_chat_rooms")
public class CareChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatSeq;
    
    @Column(nullable = false)
    private String chatName;
    
    @Column(nullable = false)
    private Integer chatMembers;
    
    @Column(columnDefinition = "DATETIME DEFAULT CURRENTTIMESTAMP")
    private LocalDateTime createDate;
    
    private LocalDateTime updateDate;


}