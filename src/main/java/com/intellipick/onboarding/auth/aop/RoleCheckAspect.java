package com.intellipick.onboarding.auth.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final HttpServletRequest request;

    @Before("@annotation(roleCheck)")
    public void checkRole(RoleCheck roleCheck) {
        String userRole = (String) request.getAttribute("role");

        if (userRole == null || !userRole.equals(roleCheck.value())) {
            throw new SecurityException("권한이 없습니다.");
        }
    }
}
