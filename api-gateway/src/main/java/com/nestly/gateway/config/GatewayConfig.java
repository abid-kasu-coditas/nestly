package com.nestly.gateway.config;

import com.nestly.shared.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public JwtUtil jwtUtil(@Value("${nestly.jwt.secret}") String secret) {
        return new JwtUtil(secret);
    }
}
