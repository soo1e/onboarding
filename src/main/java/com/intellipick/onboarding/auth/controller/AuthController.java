package com.intellipick.onboarding.auth.controller;

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
public class AuthController {

	private final AuthService authService;

	@PostMapping("/api/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@RoleCheck("ROLE_ADMIN")
	@GetMapping("/admin")
	public ResponseEntity<String> adminAccess() {
		return ResponseEntity.ok("관리자만 접근 가능");
	}

}
