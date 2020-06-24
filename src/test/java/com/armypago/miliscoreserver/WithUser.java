package com.armypago.miliscoreserver;

import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserSecurityContextFactory.class)
public @interface WithUser {

    String name() default "junyoung";

    String email() default "cupjoo@gmail.com";

    String[] roles() default {"USER"};

    MilitaryServiceStatus status();
}
