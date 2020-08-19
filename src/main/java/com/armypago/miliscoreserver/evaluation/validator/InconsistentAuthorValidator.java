package com.armypago.miliscoreserver.evaluation.validator;

import com.armypago.miliscoreserver.config.auth.LoginUserArgumentResolver;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.EvaluationRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

@RequiredArgsConstructor
public class InconsistentAuthorValidator
        implements ConstraintValidator<InconsistentAuthor, Long> {

    private final LoginUserArgumentResolver resolver;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;

    @Override
    public void initialize(InconsistentAuthor constraintAnnotation) {}

    @Override
    public boolean isValid(Long evaluationId, ConstraintValidatorContext context) {
        Optional<User> sessionUser = userRepository
                .findByEmail(resolver.getSessionUser().getEmail());
        Optional<Evaluation> evaluation = evaluationRepository.findById(evaluationId);

        return sessionUser.isPresent() && evaluation.isPresent() &&
                sessionUser.get().equals(evaluation.get().getAuthor());
    }
}
