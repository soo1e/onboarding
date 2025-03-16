package com.intellipick.onboarding.auth.service;

import com.intellipick.onboarding.auth.controller.AdminController;
import com.intellipick.onboarding.auth.dto.SignupResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.exception.AccessDeniedException;
import com.intellipick.onboarding.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminController adminController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("관리자 권한을 부여할 수 있다.")
    void grantAdminRole_Success() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn(
            (Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN")) // ✅ 수정됨
        );
        SecurityContextHolder.setContext(securityContext);

        User user = User.builder()
            .id(1L)
            .username("testuser")
            .nickname("testnickname")
            .role(Role.ROLE_USER)
            .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        SignupResponse response = (SignupResponse) adminController.grantAdminRole(1L).getBody();

        assertNotNull(response);
        assertEquals("testuser", response.username());

        assertEquals(List.of("ADMIN"), response.roles());
    }

    @Test
    @DisplayName("일반 사용자가 관리자 권한 부여를 시도하면 AccessDeniedException 발생")
    void grantAdminRole_Fail_UnauthorizedUser() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn(
            (Collection) List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.setContext(securityContext);

        AccessDeniedException thrownException = assertThrows(
            AccessDeniedException.class,
            () -> adminController.grantAdminRole(1L)
        );

        assertEquals("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.", thrownException.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 권한을 부여하려 하면 IllegalArgumentException 발생")
    void grantAdminRole_Fail_UserNotFound() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getAuthorities()).thenReturn(
            (Collection) List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException thrownException = assertThrows(
            IllegalArgumentException.class,
            () -> adminController.grantAdminRole(99L)
        );

        assertEquals("해당 사용자를 찾을 수 없습니다.", thrownException.getMessage());
    }
}
