package com.project.fatcat.users.service;

import java.io.IOException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.fatcat.entity.User;
import com.project.fatcat.entity.enums.UserRole;
import com.project.fatcat.users.dto.SignupDTO;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 🔹 회원가입
    public User register(SignupDTO dto) throws IOException {
        // 비밀번호 확인
        if (!dto.getUserPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String profilePath = ""; // TODO: 파일 업로드 구현 시 변경
        String vetLicensePath = "";

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
                //.vetLicenseImage(vetLicensePath)
                .build();

        return userRepository.save(user);
    }

   
    
//    private String saveFile(MultipartFile file, String folder) throws IOException {
//        if (file != null && !file.isEmpty()) {
//            String uploadDir = "uploads/" + folder + "/";
//            File dir = new File(uploadDir);
//            if (!dir.exists()) dir.mkdirs();
//
//            String filePath = uploadDir + System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            file.transferTo(new File(filePath));
//            return filePath;
//        }
//        return null;
//    }
}
