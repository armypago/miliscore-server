package com.armypago.miliscoreserver.branch.validator;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.branch.dto.BranchDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class NameValidator implements Validator {

    private final BranchRepository branchRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(BranchDetailDto.Request.class);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        // TODO validate 제대로 적용안됨 (create, update)
        BranchDetailDto.Request branchRequestDto = (BranchDetailDto.Request) target;
        branchRepository.findByName(branchRequestDto.getName()).ifPresent(b->{
            errors.rejectValue("nickname", "wrong.value",
                    "이미 존재하는 병과명입니다.");
        });
    }
}
