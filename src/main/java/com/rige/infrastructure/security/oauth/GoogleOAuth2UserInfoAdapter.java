package com.rige.infrastructure.security.oauth;

import com.rige.application.port.out.OAuth2UserInfoPort;
import com.rige.domain.models.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfoAdapter implements OAuth2UserInfoPort {

    @Override
    public OAuth2UserInfo extractUserInfo(Map<String, Object> attributes) {
        return new OAuth2UserInfo() {

            @Override
            public String getName() {
                return safeGet(attributes, "name");
            }

            @Override
            public String getEmail() {
                return safeGet(attributes, "email");
            }

            @Override
            public String getPictureUrl() {
                return safeGet(attributes, "picture");
            }

            @Override
            public boolean isEmailVerified() {
                Object v = attributes.get("email_verified");
                if (v instanceof Boolean) return (Boolean) v;
                if (v instanceof String) return Boolean.parseBoolean((String) v);
                return false;
            }

            private String safeGet(Map<String, Object> map, String key) {
                Object v = map.get(key);
                return v == null ? null : v.toString();
            }
        };
    }
}
