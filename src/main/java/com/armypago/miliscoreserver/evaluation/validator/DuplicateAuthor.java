package com.armypago.miliscoreserver.evaluation.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DuplicateAuthorValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateAuthor {

    String message() default "이미 평가를 작성한 사용자입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}