package com.btc.web.config;


import com.btc.web.props.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Eric on 5/21/15.
 */
@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.btc.web"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("basicAuthenticationProvider")
    AuthenticationProvider basicAuthenticationProvider;

    @Autowired
    @Qualifier("devAuthenticationProvider")
    AuthenticationProvider devAuthenticationProvider;

    @Autowired
    @Qualifier("adminAuthenticationProvider")
    AuthenticationProvider adminAuthenticationProvider;

    @Value("${basic.auth.enabled}")
    private boolean basicAuthEnabled;


    @Autowired
    PropertyService propertyService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        if (basicAuthEnabled) {
//
//            http
//                    .csrf().disable()
//                    .authorizeRequests()
//                    .antMatchers("/webjars/**").permitAll()
//                    .antMatchers("/css/**").permitAll()
//                    .antMatchers("/fonts/**").permitAll()
//                    .antMatchers("/images/**").permitAll()
//                    .antMatchers("/js/**").permitAll()
//                    .antMatchers("*/favicon.ico").permitAll()
//                    .antMatchers("/**").hasAnyAuthority("dev", "admin","customer_service")
//                    .anyRequest().authenticated();
//
//            http
//                    .formLogin()
//                    .loginPage("/login")
//                    .permitAll()
//                    .and()
//                    .logout()
//                    .permitAll();
//
//            http
//                    .authorizeRequests()
//                    .antMatchers("/ws/**")
//                    .authenticated()
//                    .and().httpBasic();
//
//        } else {
//
//            http
//                    .csrf().disable()
//                    .authorizeRequests()
//                    .antMatchers("/ws/**").permitAll()
//                    .antMatchers("/test/**").permitAll()
//                    .antMatchers("/webjars/**").permitAll()
//                    .antMatchers("/css/**").permitAll()
//                    .antMatchers("/fonts/**").permitAll()
//                    .antMatchers("/images/**").permitAll()
//                    .antMatchers("/js/**").permitAll()
//                    .antMatchers("*/favicon.ico").permitAll()
//                    .antMatchers("/**").hasAnyAuthority("dev", "admin","customer_service")
//                    .anyRequest().authenticated();
//
//            http
//                    .formLogin()
//                    .loginPage("/login")
//                    .permitAll()
//                    .and()
//                    .logout()
//                    .permitAll();
//        }


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(devAuthenticationProvider);
        auth.authenticationProvider(adminAuthenticationProvider);
        auth.authenticationProvider(basicAuthenticationProvider);
    }

}
