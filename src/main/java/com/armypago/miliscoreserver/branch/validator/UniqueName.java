package com.armypago.miliscoreserver.branch.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueNameValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueName {

    String message() default "이미 존재하는 병과명입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
