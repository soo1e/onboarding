package com.intellipick.onboarding.auth.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("이미 가입된 사용자입니다.");
    }
}
