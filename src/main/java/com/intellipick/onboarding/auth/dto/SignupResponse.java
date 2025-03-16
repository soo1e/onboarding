package com.intellipick.onboarding.auth.dto;

import com.intellipick.onboarding.auth.entity.Role;
import java.util.List;

public record SignupResponse(
	String username,
	String nickname,
	List<String> roles // ✅ Enum이 아니라 문자열 리스트 반환
) {
	public SignupResponse(String username, String nickname, Role role) {
		this(username, nickname, List.of(role.name().replace("ROLE_", ""))); // ✅ "ROLE_" 제거하여 반환
	}
}
