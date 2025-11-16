package com.rige.infrastructure.config;

import com.rige.application.port.in.OAuthLoginUseCase;
import com.rige.application.port.out.OAuth2UserInfoPort;
import com.rige.application.port.out.TokenStorePort;
import com.rige.application.port.out.UserRepositoryPort;
import com.rige.infrastructure.security.JwtAuthenticationFilter;
import com.rige.infrastructure.security.TokenProviderAdapter;
import com.rige.infrastructure.security.oauth.GoogleOAuth2UserInfoAdapter;
import com.rige.infrastructure.security.oauth.OAuth2SuccessHandler;
import com.rige.infrastructure.security.oauth.OAuth2UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtConfig jwtConfig;
    private final UserRepositoryPort userRepository;
    private final OAuthLoginUseCase oAuthLoginUseCase;
    private final TokenStorePort tokenStore;

    @Bean
    public OAuth2UserInfoPort googleOAuth2UserInfoPort() {
        return new GoogleOAuth2UserInfoAdapter();
    }

    @Bean
    public TokenProviderAdapter tokenProviderAdapter() {
        return new TokenProviderAdapter(jwtConfig);
    }

    @Bean
    public OAuth2UserServiceImpl oAuth2UserService(OAuth2UserInfoPort googleOAuth2UserInfoPort) {
        return new OAuth2UserServiceImpl(googleOAuth2UserInfoPort);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(
            OAuth2UserInfoPort googleOAuth2UserInfoPort,
            TokenProviderAdapter tokenProvider
    ) {
        return new OAuth2SuccessHandler(
                oAuthLoginUseCase,
                googleOAuth2UserInfoPort,
                tokenProvider,
                tokenStore
        );
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenProviderAdapter tokenProvider) {
        return new JwtAuthenticationFilter(tokenProvider, userRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           OAuth2UserServiceImpl oAuth2UserService,
                                           OAuth2SuccessHandler oAuth2SuccessHandler,
                                           JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/oauth2/**", "/actuator/health").permitAll()
                .anyRequest().authenticated()
        );

        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService))
                .successHandler(oAuth2SuccessHandler)
        );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
