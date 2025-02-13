package com.intellipick.onboarding.auth.controller;

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
public class UserController {

	private final UserService userService;

	@PostMapping("/api/signup")
	public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
		return ResponseEntity.ok(userService.signup(request));
	}

}
