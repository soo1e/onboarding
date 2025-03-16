package com.intellipick.onboarding.auth.controller;

import com.intellipick.onboarding.auth.dto.SignupResponse;
import com.intellipick.onboarding.auth.entity.Role;
import com.intellipick.onboarding.auth.entity.User;
import com.intellipick.onboarding.auth.exception.AccessDeniedException;
import com.intellipick.onboarding.auth.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "관리자 API", description = "관리자 권한을 관리하는 API")
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;

    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<?> grantAdminRole(@PathVariable Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException();
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        User updatedUser = User.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .nickname(user.getNickname())
            .role(Role.ROLE_ADMIN)
            .build();

        userRepository.save(updatedUser);

        return ResponseEntity.ok(new SignupResponse(updatedUser.getUsername(), updatedUser.getNickname(), updatedUser.getRole()));
    }
}
