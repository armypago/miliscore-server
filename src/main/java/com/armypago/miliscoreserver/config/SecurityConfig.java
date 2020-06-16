package com.armypago.miliscoreserver.config;

import com.armypago.miliscoreserver.domain.user.Role;
import com.armypago.miliscoreserver.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    // TODO 로그인 안해도 Branch 조회 가능하게
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile")
                        .permitAll()
                    .antMatchers("/api/v1/branch/**")
                        .hasRole(Role.USER.name())
                    .anyRequest()
                        .authenticated()
                .and()
                        .oauth2Login()
                        .userInfoEndpoint()
                        .userService(userService);
    }
}
