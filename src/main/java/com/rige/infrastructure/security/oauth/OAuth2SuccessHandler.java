package com.rige.infrastructure.security.oauth;

import com.rige.application.port.in.OAuthLoginUseCase;
import com.rige.application.port.out.OAuth2UserInfoPort;
import com.rige.application.port.out.TokenStorePort;
import com.rige.domain.models.OAuth2UserInfo;
import com.rige.domain.models.Token;
import com.rige.domain.models.User;
import com.rige.infrastructure.security.TokenProviderAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthLoginUseCase oAuthLoginUseCase;
    private final OAuth2UserInfoPort userInfoPort;
    private final TokenProviderAdapter tokenProvider;
    private final TokenStorePort tokenStore;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        OAuth2UserInfo userInfo = userInfoPort.extractUserInfo(oauthUser.getAttributes());

        User user = oAuthLoginUseCase.loginWithOAuth(userInfo);

        String access = tokenProvider.generateAccessToken(user);
        String refresh = UUID.randomUUID().toString();

        tokenStore.save(new Token(refresh, user.getId(), Instant.now().plusSeconds(604800)));

        response.setContentType("application/json");
        response.getWriter().write("""
            {
              "accessToken": "%s",
              "refreshToken": "%s"
            }
            """.formatted(access, refresh));
    }
}