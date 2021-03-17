package com.openoa.admin.service.impl;

import com.openoa.admin.entity.User;
import com.openoa.admin.repository.AuthUserRepository;
import com.openoa.admin.service.UserService;
import com.openoa.admin.vo.UserVo;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class AuthUserDetailServiceImpl implements UserDetailsService, UserService {
    @Resource
    private AuthUserRepository authUserRepository;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User authUserDetails = new User();
        authUserDetails.setUsername(s);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("enabled", "accountNonExpired", "credentialsNonExpired", "accountNonLocked");
        Example<User> example = Example.of(authUserDetails, matcher);
        Optional<User> optionalAuthUserDetails = authUserRepository.findOne(example);
        if (optionalAuthUserDetails.isPresent()) {
            User user = optionalAuthUserDetails.get();
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Integer add(UserVo userVo) {
        User user = new User();
        user.setUsername(userVo.username);
        user.setPassword(userVo.password);
        user.setFirstName(userVo.firstName);
        user.setLastName(userVo.lastName);
        user.setEmail(userVo.email);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        return authUserRepository.save(user).getId();
    }
}
