package com.rige.infrastructure.security.oauth;

import com.rige.application.port.out.OAuth2UserInfoPort;
import com.rige.domain.models.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final OAuth2UserInfoPort userInfoPort;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Copiar atributos (NUNCA modificar el original)
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());

        System.out.println("### GOOGLE ATTRIBUTES ###");
        attributes.forEach((k, v) -> System.out.println(k + " = " + v));


        // Normalizar
        OAuth2UserInfo userInfo = userInfoPort.extractUserInfo(attributes);
        attributes.put("normalized_oauth_user_info", userInfo);

        // Roles
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_USER"));

        // Google siempre define el name attribute en la configuración
        String userNameAttribute =
                userRequest.getClientRegistration()
                        .getProviderDetails()
                        .getUserInfoEndpoint()
                        .getUserNameAttributeName();

        return new DefaultOAuth2User(authorities, attributes, userNameAttribute);
    }
}