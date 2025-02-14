package com.intellipick.onboarding.auth.service;

import com.intellipick.onboarding.auth.dto.LoginRequest;
import com.intellipick.onboarding.auth.dto.LoginResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.repository.UserRepository;
import com.intellipick.onboarding.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("로그인 성공 시 JWT 토큰을 정상 발급한다.")
    void login_Success() {
        LoginRequest request = new LoginRequest("testuser", "password");

        User user = User.builder()
            .id(1L)
            .username("testuser")
            .password("encodedPassword")
            .nickname("testnickname")
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user.getUsername(), user.getRole().name())).thenReturn("mockedToken");

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mockedToken", response.token());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 로그인 시 예외가 발생한다.")
    void login_Fail_UserNotFound() {
        LoginRequest request = new LoginRequest("testuser", "password");

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}
