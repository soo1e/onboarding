package com.intellipick.onboarding.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.intellipick.onboarding.auth.dto.LoginRequest;
import com.intellipick.onboarding.auth.dto.LoginResponse;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.exception.InvalidCredentialsException;
import com.intellipick.onboarding.auth.repository.UserRepository;
import com.intellipick.onboarding.auth.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final PasswordEncoder passwordEncoder;


	public LoginResponse login(LoginRequest request) {
		User user = userRepository.findByUsername(request.username())
			.orElseThrow(InvalidCredentialsException::new);

		// 비밀번호 검증
		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new InvalidCredentialsException();
		}

		String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

		return new LoginResponse(token);
	}
}
