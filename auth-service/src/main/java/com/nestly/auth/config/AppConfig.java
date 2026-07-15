package com.nestly.auth.config;

import com.nestly.shared.util.JwtUtil;
import com.nestly.shared.util.S3PresignHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public JwtUtil jwtUtil(@Value("${nestly.jwt.secret}") String secret) {
        return new JwtUtil(secret);
    }

    @Bean
    public S3PresignHelper s3PresignHelper(@Value("${nestly.s3.region}") String region,
                                           @Value("${nestly.s3.access-key}") String accessKey,
                                           @Value("${nestly.s3.secret-key}") String secretKey,
                                           @Value("${nestly.s3.bucket}") String bucket,
                                           @Value("${nestly.s3.endpoint:}") String endpoint) {
        return new S3PresignHelper(region, accessKey, secretKey, bucket, endpoint);
    }
}
