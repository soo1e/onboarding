package com.intellipick.onboarding.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/admin")
	@Operation(summary = "관리자 접근 API", description = "관리자만 접근 가능한 페이지입니다. 관리자 ID : admin, Password : qwer1234")
	public ResponseEntity<String> adminAccess() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("[AdminController] 현재 인증 정보: " + auth);
		return ResponseEntity.ok("관리자만 접근 가능합니다! 당신은 관리자군요");
	}
}
