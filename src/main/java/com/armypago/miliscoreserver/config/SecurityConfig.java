package com.armypago.miliscoreserver.config;

import com.armypago.miliscoreserver.domain.user.Role;
import com.armypago.miliscoreserver.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    
    // TODO evaluation 제어 추가 후 API 테스트
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile")
                        .permitAll()
                    .antMatchers(HttpMethod.POST, "/api/v1/branch/**").hasRole(Role.MANAGER.name())
                    .antMatchers(HttpMethod.PUT, "/api/v1/branch/**").hasRole(Role.MANAGER.name())
                    .antMatchers(HttpMethod.GET, "/api/v1/branch/**").permitAll()
                        .anyRequest()
                            .authenticated()
                .and()
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(userService);
    }
}
