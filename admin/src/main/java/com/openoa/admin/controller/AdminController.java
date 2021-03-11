package com.openoa.admin.controller;

import com.openoa.admin.entity.AuthUserDetails;
import com.openoa.admin.service.impl.AuthUserDetailServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/api")
public class AdminController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AuthUserDetailServiceImpl authUserDetailService;
    @Value("${jwt.subject}")
    private String subject;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expireSecond}")
    private Integer expireSecond;
    @Value("${jwt.rememberMeSecond}")
    private Integer rememberMeSecond;

    @RequestMapping("/userInfo")
    @ResponseBody
    AuthUserDetails userInfo() {
        AuthUserDetails userDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            userDetails = (AuthUserDetails) authUserDetailService.loadUserByUsername(currentUserName);
        }
        return userDetails;
    }

    @PostMapping("/login")
    @ResponseBody
    String login(@Param(value = "username") String username, @Param(value = "password") String password) {
        String tokenHead = "Bearer ";
        UserDetails user = authUserDetailService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> authorities = new ArrayList<>();
        user.getAuthorities().forEach(grantedAuthority -> {
            authorities.add(grantedAuthority.getAuthority());
        });
        String token = Jwts.builder().setSubject(subject)
                .claim("username", user.getUsername())
                .claim("authorities", String.join(",", authorities))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireSecond * 1000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
        redisTemplate.opsForValue().set(token, user.getUsername());

        return tokenHead + token;
    }
}
