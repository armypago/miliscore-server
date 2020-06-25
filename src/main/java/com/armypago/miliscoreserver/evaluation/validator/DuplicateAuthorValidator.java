package com.armypago.miliscoreserver.evaluation.validator;

import com.armypago.miliscoreserver.evaluation.EvaluationRepository;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetail;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class DuplicateAuthorValidator
        implements ConstraintValidator<DuplicateAuthor, EvaluationDetail.Request> {

    private final EvaluationRepository evaluationRepository;

    @Override
    public void initialize(DuplicateAuthor constraintAnnotation) {}

    @Override
    public boolean isValid(EvaluationDetail.Request request,
                           ConstraintValidatorContext context) {
        return !evaluationRepository
                .findByAuthorIdAndBranchId(request.getAuthorId(), request.getBranchId())
                .isPresent();
    }
}