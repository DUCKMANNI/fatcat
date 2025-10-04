package com.project.fatcat.users.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.project.fatcat.entity.OauthAccount;
import com.project.fatcat.entity.User;
import com.project.fatcat.entity.enums.UserRole;
import com.project.fatcat.users.repository.OauthAccountRepository;
import com.project.fatcat.users.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{

	private final UserRepository userRepository;
    private final OauthAccountRepository oauthAccountRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google or kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("🌐 OAuth2 로그인 시도 - provider: {}", registrationId);
        log.info("✅ attributes: {}", attributes);

        // -----------------------------
        // provider별 정보 추출
        // -----------------------------
        String tmpEmail = null;
        String tmpName = null;
        String tmpPicture = null;
        String tmpProviderUserId = null;

        if ("google".equals(registrationId)) {
            tmpEmail = (String) attributes.get("email");
            tmpName = (String) attributes.get("name");
            tmpPicture = (String) attributes.get("picture");
            tmpProviderUserId = (String) attributes.get("sub");
            
            log.info("tmpName : " + tmpName);

        } else if ("kakao".equals(registrationId)) {
            tmpProviderUserId = attributes.get("id").toString();

            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                tmpEmail = (String) kakaoAccount.get("email");

                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null) {
                    tmpName = (String) profile.get("nickname");
                    tmpPicture = (String) profile.get("profile_image_url");
                }
            }
        }

        // -----------------------------
        // final 변수로 고정 (람다 안에서도 안전)
        // -----------------------------
        final String providerUserId = tmpProviderUserId;
        final String email = (tmpEmail != null) ? tmpEmail : registrationId + "_" + providerUserId + "@social.local";
        final String name = (tmpName != null) ? tmpName : registrationId + "사용자";
        final String picture = tmpPicture;
        log.info("name : " + name);
        // -----------------------------
        // 1) User 조회 or 생성
        // -----------------------------
        User user = userRepository.findByUserEmail(email).orElseGet(() -> {
            //log.info("🆕 신규 {} 사용자 생성: {}", registrationId, email);
            User newUser = User.builder()
                    .userEmail(email)
                    .userPassword("SOCIAL")  // 더미 패스워드
                    .userName(name)
                    .nickname(name)
                    .phoneNumber("000-0000-0000")
                    .address1("")
                    .address2("")
                    .zipCode("")
                    .latitude(0.0)
                    .longitude(0.0)
                    .role(UserRole.ROLE_USER)
                    .userType("A")
                    .profileImage(picture)
                    .isDeleted(false)
                    .build();
            return userRepository.save(newUser);
        });

        // -----------------------------
        // 2) OauthAccount 매핑
        // -----------------------------
        
        log.info("user.name : " + user.getUserName());
        oauthAccountRepository.findByProviderAndProviderUserId(registrationId, providerUserId)
                .orElseGet(() -> {
                   // log.info("🔗 OAuth 계정 연결: provider={} / email={}", registrationId, email);
                    OauthAccount account = OauthAccount.builder()
                            .user(user)
                            .provider(registrationId)
                            .providerUserId(providerUserId)
                            .providerEmail(email)
                            .build();
                    return oauthAccountRepository.save(account);
                });

        // -----------------------------
        // 3) CustomUserDetails 반환
        // -----------------------------
        return new CustomUserDetails(user, attributes);
    }

}
