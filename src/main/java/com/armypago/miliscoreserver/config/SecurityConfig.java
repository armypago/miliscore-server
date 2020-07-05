package com.armypago.miliscoreserver.config;

import com.armypago.miliscoreserver.domain.user.Role;
import com.armypago.miliscoreserver.user.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;
    
    // TODO evaluation 제어 추가 후 API 테스트
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .antMatchers("/api/**", "/h2-console/**")
                .permitAll();
//                .and()
//                    .authorizeRequests()
//                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile")
//                        .permitAll()
//                    .antMatchers(HttpMethod.POST, "/api/v1/branch/**").hasRole(Role.MANAGER.name())
//                    .antMatchers(HttpMethod.PUT, "/api/v1/branch/**").hasRole(Role.MANAGER.name())
//                    .antMatchers(HttpMethod.GET, "/api/v1/branch/**").permitAll()
//                        .anyRequest()
//                            .authenticated()
//                .and()
//                    .oauth2Login()
//                    .userInfoEndpoint()
//                    .userService(customOAuth2UserService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/api/**", "/v2/api-docs", "/configuration/ui",
                "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**","/swagger/**");
    }
}
