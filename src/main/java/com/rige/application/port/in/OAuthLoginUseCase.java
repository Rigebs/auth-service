package com.rige.application.port.in;


import com.rige.domain.models.OAuth2UserInfo;
import com.rige.domain.models.User;

public interface OAuthLoginUseCase {
    User loginWithOAuth(OAuth2UserInfo userInfo);
}