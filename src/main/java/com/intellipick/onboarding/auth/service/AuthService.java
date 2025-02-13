package com.intellipick.onboarding.auth.service;

import org.springframework.stereotype.Service;

import com.intellipick.onboarding.auth.dto.LoginRequest;
import com.intellipick.onboarding.auth.dto.LoginResponse;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.repository.UserRepository;
import com.intellipick.onboarding.auth.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByUsername(request.username())
			.orElseThrow(() -> new IllegalArgumentException("유효하지 않는 사용자입니다."));

		String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		return new LoginResponse(token);
	}
}
