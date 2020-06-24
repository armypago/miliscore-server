package com.armypago.miliscoreserver.config.auth.validator;

import com.armypago.miliscoreserver.config.auth.LoginUserArgumentResolver;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class InconsistentUserValidator
        implements ConstraintValidator<InconsistentUser, Long> {

    private final LoginUserArgumentResolver resolver;
    private final UserRepository userRepository;

    @Override
    public void initialize(InconsistentUser constraintAnnotation) {}

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<User> sessionUser = userRepository.findByEmail(resolver.getSessionUser().getEmail());
        Optional<User> user = userRepository.findById(value);

        return sessionUser.isPresent() && user.isPresent()
                && sessionUser.get().equals(user.get());
    }
}