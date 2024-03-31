package com.application.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${chat.websocket.endpoint}")
    private String endPoint;
    @Value("${chat.websocket.dest-prefix}")
    private String destPrefix;
    @Value("${chat.websocket.broker-prefix}")
    private String brokerPrefix;




    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(this.endPoint)
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.addEndpoint(this.endPoint)
                .setAllowedOrigins("*");
        registry.addEndpoint("/socket.io")
                .setAllowedOriginPatterns("*")
                .withSockJS();
        registry.addEndpoint("/chat")
                .setAllowedOrigins("*");
    }



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes(this.destPrefix);
        registry.enableSimpleBroker(this.brokerPrefix);
    }

}