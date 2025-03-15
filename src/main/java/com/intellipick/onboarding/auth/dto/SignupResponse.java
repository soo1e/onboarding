package com.intellipick.onboarding.auth.dto;

import com.intellipick.onboarding.auth.entity.Role;
import java.util.List;

public record SignupResponse(
	String username,
	String nickname,
	List<RoleWrapper> roles
) {
	public record RoleWrapper(String role) {
	}

	public SignupResponse(String username, String nickname, Role role) {
		this(username, nickname, List.of(new RoleWrapper(role.name().replace("ROLE_", ""))));
	}
}
