package com.armypago.miliscoreserver.config.auth.dto;

import com.armypago.miliscoreserver.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

import static java.util.Collections.singletonList;

// TODO : SessionUser 필요 정보 수정
// TODO : 실명제?

@Getter
public class SessionUser implements UserDetails {

    private String name;
    private String email;
    private List<GrantedAuthority> authorities;

    public SessionUser(User user) {
        name = user.getName();
        email = user.getEmail();
        authorities = singletonList(
                new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
