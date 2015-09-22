package com.btc.web.auth;

/**
 * Created by Eric on 5/29/15.
 */

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
 * Created by Eric on 3/24/15.
 */
@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

    private Md5PasswordEncoder md5PasswordEncoder;
    private List<String> basicAuthorities;

    @Value("${sec.basic.user}")
    private String user;

    @Value("${sec.basic.password}")
    private String password;

    @Value("${sec.basic.authorities}")
    private String authorities;

    @PostConstruct
    public void init() {
        md5PasswordEncoder = new Md5PasswordEncoder();
        basicAuthorities = Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(authorities));
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordAuthenticationToken usernamePass = (UsernamePasswordAuthenticationToken) authentication;

        String passedInUsername = (String) usernamePass.getPrincipal();
        String encodedPassword = md5EncodedPassword((String) usernamePass.getCredentials());

        if (user.equals(passedInUsername) && password.equals(encodedPassword)) {

            Set<GrantedAuthority> grantedAuthorities = basicAuthorities.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            return new UsernamePasswordAuthenticationToken(passedInUsername, (String) usernamePass.getCredentials(), Collections.unmodifiableSet(grantedAuthorities));

        }

        return null;

    }

    private String md5EncodedPassword(String password) {
        if (!Strings.isNullOrEmpty(password)) {
            return (md5PasswordEncoder.encodePassword(password, null));
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
