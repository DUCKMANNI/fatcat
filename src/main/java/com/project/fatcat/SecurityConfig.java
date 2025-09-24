package com.project.fatcat;


import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//	@Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
//                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
//            .oauth2Login(oauth2Login -> oauth2Login
//                .loginPage("/login") // OAuth2 로그인 페이지를 직접 정의할 경우
//                .defaultSuccessUrl("/")
//                .failureUrl("/loginFailure")
//                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
//                    .userService(oAuth2UserService()) // 사용자 정보 처리 서비스 등록
//                )
//            )
//            .logout(logout -> logout
//                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
//                .logoutSuccessUrl("/")
//                .invalidateHttpSession(true))
//            .csrf(csrf -> csrf.disable());
//        
//        return http.build();
//    }
//	
//	@Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
//        // 이 서비스에서 OAuth2 사용자 정보를 처리합니다.
//        // 여기서 네이버, 카카오, 구글 사용자 정보를 공통된 포맷으로 변환해야 합니다.
//        // 예를 들어, 이메일, 이름, 프로필 이미지 등을 추출하여 DB에 저장하거나 업데이트합니다.
//        
//        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//        return (userRequest) -> {
//            OAuth2User oAuth2User = delegate.loadUser(userRequest);
//            String provider = userRequest.getClientRegistration().getRegistrationId();
//            
//            // TODO: provider (google, kakao, naver)에 따라 사용자 정보를 추출하고
//            // 엔티티(예: User)에 매핑하는 로직을 여기에 구현해야 합니다.
//            // Map<String, Object> attributes = oAuth2User.getAttributes();
//            
//            // 추출된 정보를 기반으로 사용자 엔티티를 생성하거나 업데이트합니다.
//            // UserEntity user = new UserEntity(attributes);
//            // userRepository.save(user);
//
//            return oAuth2User; // 처리된 사용자 정보를 반환합니다.
//        };
//    }
//    
//    // 이 부분은 스프링 시큐리티 6.x 버전에서 필요
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
//        return new ProviderManager(Collections.singletonList(authenticationProvider));
//    }
//	
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//	
//	@Bean
//	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
//		return authenticationConfiguration.getAuthenticationManager();
//	}
}
