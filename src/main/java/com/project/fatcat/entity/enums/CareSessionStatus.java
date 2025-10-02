package com.project.fatcat.entity.enums;

public enum CareSessionStatus {

	REQUESTED("요청됨"),
    CONFIRMED("확정됨"),
    CANCELLED("취소됨");

    private final String displayName;

    CareSessionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
