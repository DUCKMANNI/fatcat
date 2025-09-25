package com.project.fatcat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // '/images/cat-profiles/' 경로로 들어오는 요청을
        // 실제 파일이 저장된 'file:///c:/fatcat-uploads/' 경로로 연결합니다.
        registry.addResourceHandler("/images/cat-profiles/**")
                .addResourceLocations("file:///" + uploadDir);
    }
}
