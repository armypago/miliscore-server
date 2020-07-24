package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.branch.Recruit;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Setter
@NoArgsConstructor
@Getter
public class RecruitCreate {

    private List<Data> datas;

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Data {
        private String branchName;
        private LocalDate recruitDate;

        public Recruit toEntity(Branch branch){
            return Recruit.builder().branch(branch).date(recruitDate).build();
        }
    }
}
