package com.intellipick.onboarding.auth.dto;

public record SignupRequest(
	String username,
	String password,
	String nickname
) {
}
