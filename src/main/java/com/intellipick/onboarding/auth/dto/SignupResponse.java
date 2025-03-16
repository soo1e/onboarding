package com.intellipick.onboarding.auth.dto;

import com.intellipick.onboarding.auth.entity.Role;
import java.util.List;

public record SignupResponse(
	String username,
	String nickname,
	List<String> roles
) {
	public SignupResponse(String username, String nickname, Role role) {
		this(username, nickname, List.of(role.name().replace("ROLE_", "")));
	}
}
