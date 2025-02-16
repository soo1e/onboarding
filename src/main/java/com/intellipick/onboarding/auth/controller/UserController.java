package com.intellipick.onboarding.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.intellipick.onboarding.auth.dto.SignupRequest;
import com.intellipick.onboarding.auth.dto.SignupResponse;
import com.intellipick.onboarding.auth.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@Tag(name = "회원가입 API", description = "사용자 회원가입 관련 API")
public class UserController {

	private final UserService userService;

	@PostMapping("/api/signup")
	@Operation(summary = "회원가입 API", description = "사용자의 정보를 입력받아 회원가입을 처리합니다.")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
		return ResponseEntity.ok(userService.signup(request));
	}
}
