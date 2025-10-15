package com.project.fatcat.users.dto;


import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.entity.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

	// 🔹 로그인/인증 관련
    @Email(message = "유효한 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일(아이디)은 필수 입력 항목입니다.")
    private String userEmail;        // 로그인 아이디 (이메일)
    
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    private String userPassword;     // 비밀번호
    
    // 비밀번호 확인은 DTO에서는 유효성 검사 제외 (Controller나 Service에서 직접 비교)
    private String passwordConfirm;	 // 비밀번호 확인

    // 🔹 기본 회원 정보
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String userName;         // 실제 이름
    
    @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
    private String nickname;         // 닉네임
    
    @NotBlank(message = "휴대폰 번호는 필수 입력 항목입니다.")
    private String phoneNumber;      // 휴대폰 번호

    // 🔹 주소/위치 (모두 필수 항목인 경우 NotBlank 추가)
    @NotBlank(message = "우편번호는 필수 입력 항목입니다.")
    private String zipCode;          
    
    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String address1;         
    
    private String address2;         
    
    // latitude, longitude는 주소 검색 성공 시 값이 들어가므로, DB NOT NULL에 맞춰 @NotNull 등을 추가할 수 있습니다.
    private Double latitude;         // 위도 (지도 API로 변환)
    private Double longitude;        // 경도

    // 🔹 유저 권한
    private UserRole role;      
    private String userType;    

    // 🚨 DTO 내부에 MultipartFile 필드는 유지 (Service에서 사용해야 하므로)
    private MultipartFile profileImageFile;
    private MultipartFile vetLicenseImageFile;
}