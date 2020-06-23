package com.armypago.miliscoreserver.branch.validator;

import com.armypago.miliscoreserver.branch.BranchRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String> {

    private final BranchRepository branchRepository;

    @Override
    public void initialize(UniqueName constraintAnnotation) {}

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return !branchRepository.findByName(name).isPresent();
    }
}
