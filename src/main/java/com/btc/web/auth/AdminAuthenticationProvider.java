package com.btc.web.auth;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Eric on 9/26/14.
 */
@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {

    private Md5PasswordEncoder md5PasswordEncoder;
    private List<String> adminAuthorities;

    @Value("${sec.admin.user}")
    private String user;

    @Value("${sec.admin.password}")
    private String password;

    @Value("${sec.admin.authorities}")
    private String authorities;

    @PostConstruct
    public void init() {
        md5PasswordEncoder = new Md5PasswordEncoder();
        adminAuthorities = Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(authorities));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken usernamePass = (UsernamePasswordAuthenticationToken) authentication;

        String passedInUsername = (String) usernamePass.getPrincipal();
        String encodedPassword = md5EncodedPassword((String) usernamePass.getCredentials());

        if (user.equals(passedInUsername) && password.equals(encodedPassword)) {

            Set<GrantedAuthority> grantedAuthorities = adminAuthorities.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            return new UsernamePasswordAuthenticationToken(passedInUsername, (String) usernamePass.getCredentials(), Collections.unmodifiableSet(grantedAuthorities));

        } else {

            return null;
        }

    }

    private String md5EncodedPassword(String password) {
        if (!Strings.isNullOrEmpty(password)) {
            return (md5PasswordEncoder.encodePassword(password, null));
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }
}
