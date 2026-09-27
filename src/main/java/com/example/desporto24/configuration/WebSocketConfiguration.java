package com.example.desporto24.configuration;

import com.example.desporto24.websocket.ChatRealtimeGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.Arrays;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    private final ChatRealtimeGateway chatRealtimeGateway;
    private final String[] allowedOrigins;

    public WebSocketConfiguration(
            ChatRealtimeGateway chatRealtimeGateway,
            @Value("${desporto.cors.allowed-origins}") String allowedOrigins) {
        this.chatRealtimeGateway = chatRealtimeGateway;
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toArray(String[]::new);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatRealtimeGateway, "/ws/chat")
                .setAllowedOrigins(allowedOrigins);
    }
}
