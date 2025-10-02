package com.project.fatcat.users.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.fatcat.entity.User;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails{

	private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
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
        return user.getUserName(); // 이메일을 로그인 아이디로 사용
    }
    
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
                // 👉 UserType 이 A냐 B냐에 따라 다르게
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
