package com.intellipick.onboarding.auth.dto;

public record LoginRequest(
	String username,
	String password
) {
}
