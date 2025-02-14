package com.intellipick.onboarding.auth.security;

import com.intellipick.onboarding.auth.aop.RoleCheck;
import com.intellipick.onboarding.auth.aop.RoleCheckAspect;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Aspect
class RoleCheckAspectTest {

	@Mock
	private HttpServletRequest request;

	private RoleCheckAspect roleCheckAspect;

	@Mock
	private RoleCheck roleCheck;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		roleCheckAspect = new RoleCheckAspect(request);
	}

	@Test
	@DisplayName("관리자 페이지 접근 성공")
	void checkRole_Success_AdminAccess() {
		when(request.getAttribute("role")).thenReturn("ROLE_ADMIN");
		when(roleCheck.value()).thenReturn("ROLE_ADMIN");

		assertDoesNotThrow(() -> roleCheckAspect.checkRole(roleCheck));
	}

	@Test
	@DisplayName("일반 사용자가 관리자 페이지 접근 시 예외 발생")
	void checkRole_Fail_UserAccessDenied() {
		when(request.getAttribute("role")).thenReturn("ROLE_USER");
		when(roleCheck.value()).thenReturn("ROLE_ADMIN");
		assertThrows(SecurityException.class, () -> roleCheckAspect.checkRole(roleCheck));
	}
}
