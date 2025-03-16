package com.intellipick.onboarding.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        System.out.println("🔵 요청 URI: " + requestURI);

        String token = jwtUtil.resolveToken(httpRequest);
        System.out.println("🔵 추출된 토큰: " + token);

        if (token == null) {
            System.out.println("❌ JWT 토큰이 요청에 없음");
            chain.doFilter(request, response);
            return;
        }

        try {
            if (!jwtUtil.validateToken(token)) {
                System.err.println("❌ JWT 검증 실패: " + token);
                sendErrorResponse(httpResponse, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
                return;
            }

            String username = jwtUtil.getUsername(token);
            String role = jwtUtil.getUserRole(token);

            System.out.println("✅ JWT 검증 성공: " + username + " (" + role + ")");

            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
            User userDetails = new User(username, "", Collections.singletonList(authority));
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (Exception e) {
            System.err.println("❌ JWT 필터에서 오류 발생: " + e.getMessage());
            sendErrorResponse(httpResponse, "INVALID_TOKEN", "유효하지 않은 인증 토큰입니다.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String errorCode, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
            "{ \"error\": { \"code\": \"" + errorCode + "\", \"message\": \"" + message + "\" } }"
        );
    }
}
