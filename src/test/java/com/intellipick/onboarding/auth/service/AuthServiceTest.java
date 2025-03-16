package com.intellipick.onboarding.auth.service;

import com.intellipick.onboarding.auth.dto.LoginRequest;
import com.intellipick.onboarding.auth.dto.LoginResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.exception.InvalidCredentialsException;
import com.intellipick.onboarding.auth.repository.UserRepository;
import com.intellipick.onboarding.auth.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder; // ✅ 추가됨

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
            .password("encodedPassword") // ✅ 실제 저장된 비밀번호는 암호화됨
            .nickname("testnickname")
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true); // ✅ 비밀번호 검증 추가
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

        InvalidCredentialsException thrownException = assertThrows(
            InvalidCredentialsException.class, // ✅ `IllegalArgumentException` → `InvalidCredentialsException`
            () -> authService.login(request)
        );

        assertEquals("아이디 또는 비밀번호가 올바르지 않습니다.", thrownException.getMessage());
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다.")
    void login_Fail_WrongPassword() {
        LoginRequest request = new LoginRequest("testuser", "wrongPassword");

        User user = User.builder()
            .id(1L)
            .username("testuser")
            .password("encodedPassword") // ✅ 실제 저장된 비밀번호는 암호화됨
            .nickname("testnickname")
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false); // ✅ 비밀번호 검증 실패

        InvalidCredentialsException thrownException = assertThrows(
            InvalidCredentialsException.class, // ✅ `IllegalArgumentException` → `InvalidCredentialsException`
            () -> authService.login(request)
        );

        assertEquals("아이디 또는 비밀번호가 올바르지 않습니다.", thrownException.getMessage());
    }
}
