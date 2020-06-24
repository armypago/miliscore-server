package com.armypago.miliscoreserver.evaluation.validator;

import com.armypago.miliscoreserver.evaluation.EvaluationRepository;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetailDto;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicateAuthorValidator
        implements ConstraintValidator<DuplicateAuthor, EvaluationDetailDto.EvaluationKey> {

    private final EvaluationRepository evaluationRepository;

    @Override
    public void initialize(DuplicateAuthor constraintAnnotation) {}

    @Override
    public boolean isValid(EvaluationDetailDto.EvaluationKey key,
                           ConstraintValidatorContext context) {
        return !evaluationRepository
                .findByAuthorIdAndBranchId(key.getAuthorId(), key.getBranchId())
                .isPresent();
    }
}