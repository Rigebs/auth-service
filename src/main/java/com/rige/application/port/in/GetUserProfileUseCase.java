package com.rige.application.port.in;

import com.rige.domain.models.User;

public interface GetUserProfileUseCase {
    User getCurrentUserProfile(Long userId);
}
