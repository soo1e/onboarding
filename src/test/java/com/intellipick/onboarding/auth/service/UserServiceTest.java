package com.intellipick.onboarding.auth.service;

import com.intellipick.onboarding.auth.dto.SignupRequest;
import com.intellipick.onboarding.auth.dto.SignupResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.repository.UserRepository;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입이 성공적으로 수행된다.")
    void signup_Success() {
        SignupRequest request = new SignupRequest("testuser", "password", "testnickname");

        User user = User.builder()
            .username(request.username())
            .password("encodedPassword")
            .nickname(request.nickname())
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword"); // ✅ Mock Password Encoding 설정
        when(userRepository.save(any(User.class))).thenReturn(user);

        SignupResponse response = userService.signup(request);

        assertNotNull(response);
        assertEquals("testuser", response.username());
        assertEquals(Role.ROLE_USER, response.role());
    }

    @Test
    @DisplayName("중복된 회원가입 요청 시 예외가 발생한다.")
    void signup_Fail_DuplicateUser() {
        SignupRequest request = new SignupRequest("testuser", "password", "testnickname");

        User existingUser = User.builder()
            .username(request.username())
            .password("encodedPassword")
            .nickname(request.nickname())
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(existingUser));

        IllegalArgumentException thrownException = assertThrows(
            IllegalArgumentException.class,
            () -> userService.signup(request),
            "중복된 회원가입 시 IllegalArgumentException이 발생해야 합니다."
        );

        assertEquals("이미 존재하는 사용자입니다.", thrownException.getMessage());
    }
}
