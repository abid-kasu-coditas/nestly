package com.nestly.shared.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil("this-is-a-dev-secret-key-with-32+chars!!");

    @Test
    void generateAndParseRoundTrip() {
        String token = jwtUtil.generateToken("user-123", "OWNER", "owner@test.com");

        Claims claims = jwtUtil.parseToken(token);

        assertThat(claims.getSubject()).isEqualTo("user-123");
        assertThat(claims.get("role", String.class)).isEqualTo("OWNER");
        assertThat(claims.get("email", String.class)).isEqualTo("owner@test.com");
    }

    @Test
    void garbageTokenIsRejected() {
        assertThatThrownBy(() -> jwtUtil.parseToken("not-a-real-token"))
                .isInstanceOf(JwtException.class);
    }
}
