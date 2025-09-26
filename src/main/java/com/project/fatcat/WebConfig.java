package com.project.fatcat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Value("${file.upload.dir}")
//    private String uploadDir;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // '/images/cat-profiles/' 경로로 들어오는 요청을
//        // 실제 파일이 저장된 'file:///c:/fatcat-uploads/' 경로로 연결합니다.
//        registry.addResourceHandler("/images/cat-profiles/**")
//                .addResourceLocations("file:///" + uploadDir);
//    }
    
	
	@Value("${file.upload.dir}")
	private String uploadDir;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    // 1. 웹에서 요청하는 경로: "/images/cat-profiles/**"
	    registry.addResourceHandler("/images/cat-profiles/**") 
	            // 2. 실제 파일이 저장된 디스크 경로: "file:///c:/fatcat-uploads/" (uploadDir 값)
	            .addResourceLocations("file:///" + uploadDir);
	}
	
	
	
//    // application.properties에서 설정한 파일 저장 경로 (예: C:/fatcat-uploads/)
//    // 실제 경로는 application.properties를 참고하여 수정하세요.
//    private String uploadPath = "file:///C:/fatcat-uploads/"; 
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // "/uploads/**"로 들어오는 요청을 
//        // 실제 서버 경로(uploadPath)에서 찾도록 매핑합니다.
//        registry.addResourceHandler("/uploads/**")
//                .addResourceLocations(uploadPath);
//    }
}
