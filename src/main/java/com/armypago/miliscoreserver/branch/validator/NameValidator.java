package com.armypago.miliscoreserver.branch.validator;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.branch.dto.BranchDto;
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
        return NameValidator.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BranchDto branchDto = (BranchDto) target;
        boolean existName = branchRepository.findByName(branchDto.getName()).isPresent();
        if(existName){
            errors.rejectValue("nickname", "wrong.value",
                    "입력하신 닉네임을 사용할 수 없습니다.");
        }
    }
}
