package com.openoa.admin.controller;

import com.openoa.admin.entity.User;
import com.openoa.admin.service.impl.AuthUserDetailServiceImpl;
import com.openoa.admin.service.impl.JwtTokenServiceImpl;
import com.openoa.admin.ultil.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthUserDetailServiceImpl authUserDetailService;
    @Autowired
    private JwtTokenServiceImpl jwtTokenService;

    @RequestMapping("/getUserInfo")
    @ResponseBody
    R userInfo() {
        User user = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            user = (User) authUserDetailService.loadUserByUsername(currentUserName);
        }
        return R.ok(user);
    }

    @RequestMapping("/getOnlineUsers")
    @ResponseBody
    R getUserOnlineList() {
        List<String> onlineUsers = jwtTokenService.getOnlineUsers();
        return R.ok(onlineUsers);
    }

    @PostMapping("/login")
    @ResponseBody
    R login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password) throws Exception {
        UserDetails user = authUserDetailService.loadUserByUsername(username);
        if (null == user) {
            throw new Exception("用户不存在");
        }
        if (!jwtTokenService.validatePassword(password, user)) {
            throw new Exception("密码错误");
        }

        String token = jwtTokenService.login(user);
        return R.ok(token);
    }

    @GetMapping("/logout")
    void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String tokenHeader = "Authorization";
        String authHeader = httpServletRequest.getHeader(tokenHeader);
        String tokenHead = "Bearer ";
        if (null != authHeader && authHeader.startsWith(tokenHead)) {
            String authToken = authHeader.substring(tokenHead.length());
            jwtTokenService.deleteToken(authToken);
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        httpServletResponse.sendRedirect("/");
    }
}
