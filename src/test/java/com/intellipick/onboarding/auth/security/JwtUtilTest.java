package com.intellipick.onboarding.auth.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secretKey;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(secretKey);
    }

    @Test
    @DisplayName("JWT 토큰을 정상적으로 발급 및 검증할 수 있다.")
    void generateAndValidateToken_Success() {
        String token = jwtUtil.generateToken("testuser", "ROLE_USER");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));

        Claims claims = jwtUtil.extractClaims(token);
        assertEquals("testuser", claims.getSubject());
        assertEquals("ROLE_USER", claims.get("role"));
    }

    @Test
    @DisplayName("잘못된 JWT 토큰을 검증하면 실패한다.")
    void validateToken_Fail_InvalidToken() {
        assertFalse(jwtUtil.validateToken("invalidToken"));
    }
}
