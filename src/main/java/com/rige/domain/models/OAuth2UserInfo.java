package com.rige.domain.models;

public interface OAuth2UserInfo {

    String getName();
    String getEmail();
    String getPictureUrl();
    boolean isEmailVerified();
}