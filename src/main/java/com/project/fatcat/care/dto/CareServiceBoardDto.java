package com.project.fatcat.care.dto;

import com.project.fatcat.entity.CareServiceBoard;
import com.project.fatcat.entity.enums.CareBoardStatus;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class CareServiceBoardDto {

    @NotBlank(message = "제목은 필수입니다.")
    private String careTitle;

    @NotBlank(message = "내용은 필수입니다.")
    private String careContent;

    @NotBlank(message = "주소는 필수입니다.")
    private String address1;

    @NotBlank(message = "상세주소는 필수입니다.")
    private String address2;

    @NotNull(message = "위도 정보가 누락되었습니다.")
    private Double latitude;

    @NotNull(message = "경도 정보가 누락되었습니다.")
    private Double longitude;

    @NotNull(message = "가격은 필수입니다.")
    private Integer price;
    
    @NotNull(message = "사용자 정보가 누락되었습니다.")
    private Integer userSeq;

    public CareServiceBoard toEntity() {
        // ... 기존 로직 ...
        CareServiceBoard board = new CareServiceBoard();
        board.setCareTitle(this.careTitle);
        board.setCareContent(this.careContent);
        board.setAddress1(this.address1);
        board.setAddress2(this.address2);
        board.setLatitude(this.latitude);
        board.setLongitude(this.longitude);
        board.setPrice(this.price);
        board.setStatus(CareBoardStatus.OPEN);
        board.setIsDeleted(false);
        return board;
    }
}