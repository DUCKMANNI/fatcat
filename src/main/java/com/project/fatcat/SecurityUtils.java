package com.project.fatcat;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.project.fatcat.users.service.CustomUserDetails;

public class SecurityUtils {
	
	public static CustomUserDetails getCurrentUser() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth == null || !auth.isAuthenticated()) {
	        throw new IllegalStateException("로그인 필요");
	    }

	    Object principal = auth.getPrincipal();

	    if (principal instanceof CustomUserDetails userDetails) {
	        return userDetails;
	    } else {
	        throw new IllegalStateException("인증된 사용자 정보 없음");
	    }
	}
	
	
	public static Integer getCurrentUserSeq() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("로그인 필요");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getUser().getUserSeq();
        } else {
            throw new IllegalStateException("인증된 사용자 정보 없음");
        }
    }

}
