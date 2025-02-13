package com.intellipick.onboarding.auth.service;

import org.springframework.stereotype.Service;

import com.intellipick.onboarding.auth.dto.SignupRequest;
import com.intellipick.onboarding.auth.dto.SignupResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public SignupResponse signup(SignupRequest request) {

		User user = User.builder()
			.username(request.username())
			.password(request.password())
			.nickname(request.nickname())
			.role(Role.ROLE_USER)
			.build();

		userRepository.save(user);

		return new SignupResponse(user.getUsername(), user.getNickname(), user.getRole());
	}

}
