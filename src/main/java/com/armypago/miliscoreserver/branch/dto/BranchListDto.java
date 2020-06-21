package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Branch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BranchListDto {

    private Long id;
    private String name;

    public BranchListDto(Branch branch){
        id = branch.getId();
        name = branch.getName();
    }
}