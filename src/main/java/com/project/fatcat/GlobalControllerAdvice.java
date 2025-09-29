package com.project.fatcat;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalControllerAdvice {

	@ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {
        // 1. 현재 요청 경로를 가져옵니다. (예: /vetclinic)
        String requestPath = request.getRequestURI();
        
        // 2. 경로에 따라 pageTitle을 설정합니다.
        String title = mapPathToTitle(requestPath);
        
        // 3. 모델에 추가합니다.
        model.addAttribute("pageTitle", title);
    }
	
	private String mapPathToTitle(String path) {
        if (path.startsWith("/mypage")) {
            return "마이페이지";
        } else if (path.startsWith("/products")) {
            return "고양이 전용 쇼핑몰";
        } else if (path.startsWith("/shopping")) {
            return "고양이 전용 쇼핑몰";
        } else if (path.startsWith("/vetclinic")) {
            return "동물병원 정보"; // 현재 보고 있는 페이지
        } else if (path.startsWith("/care")) {
            return "돌봄서비스";
        } else if (path.startsWith("/post/list/1")) {
            return "고양이 지식방 - 수의사에게 질문하기";
        } else if (path.startsWith("/post/list/2")) {
            return "고양이 지식방 - 냥꿀팁";
        } else if (path.startsWith("/faq")) {
            return "고객센터";
        } else {
            return ""; // 기본값 (혹은 로고 옆에 아무것도 표시하지 않음)
        }
    }
}
