package com.intellipick.onboarding.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.intellipick.onboarding.auth.dto.SignupRequest;
import com.intellipick.onboarding.auth.dto.SignupResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.exception.UserAlreadyExistsException;
import com.intellipick.onboarding.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public SignupResponse signup(SignupRequest request) {
		if (userRepository.findByUsername(request.username()).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		User user = User.builder()
			.username(request.username())
			.password(passwordEncoder.encode(request.password()))
			.nickname(request.nickname())
			.role(Role.ROLE_USER)
			.build();

		userRepository.save(user);

		return new SignupResponse(user.getUsername(), user.getNickname(), user.getRole());
	}
}
