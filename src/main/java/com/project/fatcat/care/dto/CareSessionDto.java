package com.project.fatcat.care.dto;

import com.project.fatcat.entity.CareSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CareSessionDto {

    private Integer sessionSeq;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String careProviderUserName;
    private String clientUserName;
    private String status;

    // 엔티티를 DTO로 변환하는 생성자
    public CareSessionDto(CareSession session) {
        this.sessionSeq = session.getSessionSeq();
        this.startDate = session.getStartDate();
        this.endDate = session.getEndDate();

        
        if (session.getStatus() != null) {
            this.status = session.getStatus().name();
        }

       
        if (session.getSitterUser() != null) {
            this.careProviderUserName = session.getSitterUser().getUserName();
        }

        if (session.getOwnerUser() != null) {
            this.clientUserName = session.getOwnerUser().getUserName();
        }
    }
}