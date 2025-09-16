package com.project.fatcat.chat;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
	

	
	
	
	
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		
		//메세지 브로커가 topic queue 프리픽스 처리하도록
		registry.enableSimpleBroker("/topic", "/queue");
		//클라이언트에서 서버로 메세지 보낼떄 사용하는 prefix
		registry.setApplicationDestinationPrefixes("/app");
	}
	

	
	
}
