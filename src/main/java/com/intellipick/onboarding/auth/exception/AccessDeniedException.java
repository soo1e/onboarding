package com.intellipick.onboarding.auth.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다.");
    }
}
