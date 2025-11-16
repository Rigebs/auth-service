package com.rige.domain.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String pictureUrl;

    private AuthProvider provider;
    private Set<Role> roles;
    private boolean emailVerified;
}