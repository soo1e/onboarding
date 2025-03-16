package com.intellipick.onboarding.auth.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("해당 ID(" + userId + ")의 사용자를 찾을 수 없습니다.");
    }
}
