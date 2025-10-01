package com.project.fatcat.users.dto;

import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.entity.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDTO {

	// 🔹 로그인/인증 관련
    private String userEmail;        // 로그인 아이디 (이메일)
    private String userPassword;     // 비밀번호
    private String passwordConfirm;	 // 비밀번호 확

    // 🔹 기본 회원 정보
    private String userName;         // 실제 이름
    private String nickname;         // 닉네임
    private String phoneNumber;      // 휴대폰 번호

    // 🔹 주소/위치
    private String address1;         
    private String address2;         
    private String zipCode;          
    private Double latitude;         // 위도 (지도 API로 변환)
    private Double longitude;        // 경도

    // 🔹 유저 권한
    private UserRole role;      // 일반유저 / 수의사
    private String userType;    // "예비집사" (기본) → 고양이 등록 시 "냥집사"로 업데이트

    // 🔹 선택 입력
    //private String profileImageUrl;  // 프로필 사진 (선택)
    private MultipartFile profileImageFile;

    // 🔹 수의사 회원일 경우
    //private String vetLicenseImage;  // 수의사 면허 이미지 (선택)
    private MultipartFile vetLicenseImageFile;
}
