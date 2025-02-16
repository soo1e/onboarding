package com.intellipick.onboarding.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.intellipick.onboarding.auth.aop.RoleCheck;
import com.intellipick.onboarding.auth.dto.LoginRequest;
import com.intellipick.onboarding.auth.dto.LoginResponse;
import com.intellipick.onboarding.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "로그인 및 권한 검증 관련 API")
public class AuthController {

	private final AuthService authService;

	@PostMapping("/api/login")
	@Operation(summary = "로그인 API", description = "사용자의 아이디와 비밀번호를 입력받아 JWT 토큰을 반환합니다.")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@RoleCheck("ROLE_ADMIN")
	@GetMapping("/admin")
	@Operation(summary = "관리자 전용 페이지", description = "관리자 권한이 있는 사용자만 접근할 수 있습니다.")
	public ResponseEntity<String> adminAccess() {
		return ResponseEntity.ok("관리자만 접근 가능합니다! 당신은 관리자군요");
	}
}
