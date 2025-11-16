package com.rige.infrastructure.controllers.dto;

public record UserResponse(
        Long id,
        String email,
        String name,
        String role
) {}
