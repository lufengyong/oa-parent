package com.openoa.admin.filter;

import com.openoa.admin.service.impl.AuthUserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.secret}")
    private String secret;
    @Autowired
    private
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    private AuthUserDetailServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = "Authorization";
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        String tokenHead = "Bearer ";
        if (null != authHeader && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(authToken).getBody();
            if (claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
                redisTemplate.delete(authToken);
            } else {
                String username = redisTemplate.opsForValue().get(authToken);
                if (null != username && null == SecurityContextHolder.getContext().getAuthentication()) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (null != userDetails) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
