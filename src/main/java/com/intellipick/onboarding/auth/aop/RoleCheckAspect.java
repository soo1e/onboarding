package com.intellipick.onboarding.auth.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final HttpServletRequest request;

    @Before("@annotation(roleCheck)")
    public void checkRole(RoleCheck roleCheck) {
        System.out.println("[RoleCheckAspect] AOP 실행됨 - 메소드 접근 시도");

        String userRole = (String) request.getAttribute("role");
        System.out.println("[RoleCheckAspect] 요청한 사용자 역할: " + userRole);
        System.out.println("[RoleCheckAspect] 필요한 역할: " + roleCheck.value());

        if (userRole == null || !userRole.equals(roleCheck.value())) {
            System.out.println("[RoleCheckAspect] 접근 거부: 권한 없음");
            throw new SecurityException("권한이 없습니다.");
        }

        System.out.println("[RoleCheckAspect] 접근 허용됨");
    }
}
