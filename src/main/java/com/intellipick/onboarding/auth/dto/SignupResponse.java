package com.intellipick.onboarding.auth.dto;

import com.intellipick.onboarding.auth.entity.Role;

public record SignupResponse(
	String username,
	String nickname,
	Role role
) {
}
