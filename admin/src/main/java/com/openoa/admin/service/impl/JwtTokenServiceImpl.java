package com.openoa.admin.service.impl;

import com.openoa.admin.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenServiceImpl implements JwtTokenService {
    @Value("${jwt.subject}")
    private String subject;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expireSecond}")
    private Integer expireSecond;
    @Value("${jwt.rememberMeSecond}")
    private Integer rememberMeSecond;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Claims parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token).getBody();
        return claims;
    }

    @Override
    public String login(UserDetails user) {
        String token = Jwts.builder().setSubject(subject)
                .claim("username", user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireSecond * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        redisTemplate.opsForValue().set(token, user.getUsername());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenHead + " " + token;
    }

    @Override
    public List<String> getOnlineUsers() {
        List<String> onlineUsers = new ArrayList<>();
        redisTemplate.keys("*").forEach((token) -> {
            String userName = redisTemplate.opsForValue().get(token);
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(token).getBody();
                if (claims.getExpiration().after(new Date(System.currentTimeMillis()))) {
                    onlineUsers.add(userName);
                }
            } catch (Exception exp) {
                redisTemplate.delete(token);
            }
        });
        return onlineUsers.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }

    @Override
    public boolean validatePassword(String password, UserDetails user) {
        return passwordEncoder.matches(password, user.getPassword());
    }
}
