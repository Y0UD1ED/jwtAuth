package com.example.jwtAuth.dtos;

import com.example.jwtAuth.models.Role;

import java.util.Collection;

public class AuthResponse {
    JwtResponse tokens;
    Integer userId;
    Collection<Role> roles;

    public JwtResponse getTokens() {
        return tokens;
    }

    public void setTokens(JwtResponse tokens) {
        this.tokens = tokens;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
