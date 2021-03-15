package com.openoa.admin.service.impl;

import com.openoa.admin.entity.AuthUserDetails;
import com.openoa.admin.repository.AuthUserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class AuthUserDetailServiceImpl implements UserDetailsService {
    @Resource
    private
    AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthUserDetails authUserDetails = new AuthUserDetails();
        authUserDetails.setUsername(s);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.exact())
                .withIgnorePaths("enabled", "accountNonExpired", "credentialsNonExpired", "accountNonLocked");
        Example<AuthUserDetails> example = Example.of(authUserDetails, matcher);
        Optional<AuthUserDetails> optionalAuthUserDetails = authUserRepository.findOne(example);
        if (optionalAuthUserDetails.isPresent()) {
            AuthUserDetails user = optionalAuthUserDetails.get();
            return user;
        } else {
            return null;
        }
    }
}
