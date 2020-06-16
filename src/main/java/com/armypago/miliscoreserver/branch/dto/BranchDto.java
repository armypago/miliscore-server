package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Branch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class BranchDto {

    private Long id;
    private String name;

    public BranchDto(Branch branch){
        id = branch.getId();
        name = branch.getName();
    }

    public Branch toEntity(){
        return Branch.builder().name(name).build();
    }

    @Override
    public String toString() {
        return "BranchDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
