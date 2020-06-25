package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Branch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BranchSimple {

    private Long id;
    private String name;

    public BranchSimple(Branch branch){
        id = branch.getId();
        name = branch.getName();
    }
}