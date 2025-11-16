package com.rige.infrastructure.controllers;

import com.rige.application.port.in.GetUserProfileUseCase;
import com.rige.domain.models.Role;
import com.rige.domain.models.User;
import com.rige.infrastructure.controllers.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserController {

    private final GetUserProfileUseCase profileUseCase;

    @GetMapping
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        User user = profileUseCase.getCurrentUserProfile(principal.getId());

        UserResponse dto = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRoles().stream()
                        .map(Role::name)
                        .findFirst()
                        .orElse("USER")
        );

        return ResponseEntity.ok(dto);
    }
}
