package com.rige.application.port.out;


import com.rige.domain.models.OAuth2UserInfo;

import java.util.Map;

public interface OAuth2UserInfoPort {

    OAuth2UserInfo extractUserInfo(Map<String, Object> attributes);
}
