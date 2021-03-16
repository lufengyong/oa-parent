package com.openoa.admin.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtTokenService {
    Claims parseToken(String token);
    String login(UserDetails user);
    List<String> getOnlineUsers();
    void deleteToken(String token);
    boolean validatePassword(String password, UserDetails user);
}
