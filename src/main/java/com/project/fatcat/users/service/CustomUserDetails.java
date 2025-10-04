package com.project.fatcat.users.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.project.fatcat.entity.User;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
public class CustomUserDetails implements UserDetails, OAuth2User{

	private final User user;
	 private Map<String, Object> attributes; // ✅ 소셜 로그인용 추가

	// 일반 로그인 생성자
    public CustomUserDetails(User user) {
        this.user = user;
    }
    
    // 소셜 로그인 생성자
    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }
    
    // OAuth2User 인터페이스용
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        // 구글 로그인 시 `name` 혹은 userName 반환
    	
    	log.info("getUSerName : " + user.getUserName());
        return user.getUserName() != null ? user.getUserName() : (String) attributes.get("name");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getUserPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserEmail(); // 이메일을 로그인 아이디로 사용
    }
    
    public String getUserName() {
        // DB 값 우선
        if (user.getUserName() != null) {
            return user.getUserName();
        }

        // OAuth2 attributes fallback
        if (attributes != null) {
            // ✅ 구글
            if (attributes.get("name") != null) {
                return (String) attributes.get("name");
            }

            // ✅ 카카오
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                if (profile != null && profile.get("nickname") != null) {
                    return (String) profile.get("nickname");
                }
            }
        }

        // fallback
        return "사용자";
    }

    
//    public String getUserName() {
//        return user.getUserName(); // 이메일을 로그인 아이디로 사용
//    }
    
    public String getPhoneNumber() {
        return user.getPhoneNumber();
    }
    
    public String getFormattedPhoneNumber() {
        if (user.getPhoneNumber() == null) return null;
        return user.getPhoneNumber().replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    }

    public String getRole() {
        return user.getRole().toString();
    }
    
    public String getUserGrade() {
        if (user.getRole() == null) return "정보 없음";

        switch (user.getRole().toString()) {
            case "ROLE_USER":
                if ("A".equals(user.getUserType())) {
                    return "냥집사";
                } else if ("B".equals(user.getUserType())) {
                    return "예비집사";
                }
                return "일반 회원"; // fallback
            case "ROLE_ADMIN":
                return "관리자";
            case "ROLE_VET":
                return "수의사";
            default:
                return "정보 없음";
        }
    }
    
    public String getProfileImage() {
        return user.getProfileImage();
    }

    
    public String getUserType() {
    	return user.getUserType();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return !Boolean.TRUE.equals(user.getIsDeleted()); }

}
