package com.project.fatcat.care.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CareServiceBoardListDto {

    private Integer careSeq;
    private String careTitle;
    private String careContent;
    private String address1;
    private String address2;
    private Double latitude;
    private Double longitude;
    private Integer price;
    private String authorNickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    private Integer userSeq;

    public CareServiceBoardListDto(Integer careSeq, String careTitle, String careContent, String address1, String address2, Double latitude, Double longitude, Integer price, String authorNickname, LocalDateTime createDate, Integer userSeq) {
        this.careSeq = careSeq;
        this.careTitle = careTitle;
        this.careContent = careContent;
        this.address1 = address1;
        this.address2 = address2;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.authorNickname = authorNickname;
        this.createDate = createDate;
        this.userSeq = userSeq;
    }
}