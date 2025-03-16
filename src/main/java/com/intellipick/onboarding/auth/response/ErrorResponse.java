package com.intellipick.onboarding.auth.response;

public record ErrorResponse(ErrorDetail error) {
	public record ErrorDetail(String code, String message) {
	}

	public static ErrorResponse invalidCredentials() {
		return new ErrorResponse(new ErrorDetail("INVALID_CREDENTIALS", "아이디 또는 비밀번호가 올바르지 않습니다."));
	}

	public static ErrorResponse accessDenied() {
		return new ErrorResponse(new ErrorDetail("ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."));
	}
}

