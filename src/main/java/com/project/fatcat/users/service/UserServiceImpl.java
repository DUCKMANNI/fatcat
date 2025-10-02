package com.project.fatcat.users.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.fatcat.entity.User;
import com.project.fatcat.entity.enums.UserRole;
import com.project.fatcat.upload.fatcatSftp;
import com.project.fatcat.users.dto.SignupDTO;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    private static final String UPLOAD_DIR_PROFILE = "/fatcat/upload/userImage/";
    private static final String UPLOAD_DIR_LICENSE = "/fatcat/upload/vetLicense/";

 // 🔹 회원가입
    public User register(SignupDTO dto) throws IOException {
        // 비밀번호 확인
        if (!dto.getUserPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        
        String profilePath = saveFile(dto.getProfileImageFile(), UPLOAD_DIR_PROFILE);
        String vetLicensePath = saveFile(dto.getVetLicenseImageFile(), UPLOAD_DIR_LICENSE);

        System.out.println("getLatitude : " + dto.getLatitude());
        System.out.println("getLongitude : " + dto.getLongitude());

        // User 엔티티 생성
        User user = User.builder()
                .userEmail(dto.getUserEmail())
                .userPassword(passwordEncoder.encode(dto.getUserPassword()))
                .userName(dto.getUserName())
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .address1(dto.getAddress1())
                .address2(dto.getAddress2())
                .zipCode(dto.getZipCode())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .role(UserRole.ROLE_USER) 
                .userType("A")
                .vetLicenseImage(vetLicensePath)
                .profileImage(profilePath)
                .build();

        return userRepository.save(user);
    }

   
    
    private String saveFile(MultipartFile file, String uploadDir) {
        if (file == null || file.isEmpty()) return null;

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            fatcatSftp fatcatSftp = new fatcatSftp();
            fatcatSftp.sftpFileUpload(file, uploadDir, fileName);
            // 웹에서 접근 가능한 경로 리턴
            return "https://ivisus.duckdns.org:9443" + uploadDir + fileName;

        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }
    }
    
    public SignupDTO getUserInfo(String userEmail) {
        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // 👉 엔티티 -> DTO 변환
        return SignupDTO.builder()
                .userEmail(user.getUserEmail())
                .userName(user.getUserName())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .zipCode(user.getZipCode())
                .address1(user.getAddress1())
                .address2(user.getAddress2())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .build();
    }
    
  //----------------------------------------------------------아래 미진 추가-------------------------------------------------------- 
   
    @Override
    public String getUserNickNameBySeq(Integer userSeq) {
        
            
        User user = userRepository.findById(userSeq)
                            .orElseThrow(() -> new IllegalArgumentException("User not found with seq: " + userSeq));
        
        // 조회된 User 엔티티에서 userName 필드를 반환합니다.
        return user.getNickname();
    }
}
