package com.armypago.miliscoreserver;

import com.armypago.miliscoreserver.config.auth.dto.SessionUser;
import com.armypago.miliscoreserver.domain.user.Role;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class WithUserSecurityContextFactory implements
        WithSecurityContextFactory<WithUser> {

    private final UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(WithUser withUser) {
        UserDetails principal = new SessionUser(createUser(withUser));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }

    private User createUser(WithUser withUser){
        User user = User.builder()
                .name(withUser.value())
                .email(withUser.email())
                .status(withUser.status()).build();
        if(withUser.roles().length > 0){
            String role = "ROLE_"+withUser.roles()[0];
            if(role.equals(Role.MANAGER.getKey())){
                user.changeRole(Role.MANAGER);
            }
        }
        return userRepository.save(user);
    }
}
