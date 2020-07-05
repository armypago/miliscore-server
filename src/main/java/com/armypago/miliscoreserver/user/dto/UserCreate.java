package com.armypago.miliscoreserver.user.dto;

import com.armypago.miliscoreserver.branch.dto.BranchSimple;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserCreate {

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Form {
        private Long userId;
        private List<String> status;
        private List<EducationSimple> education;
        private List<BranchSimple> branch;
    }

    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class EducationSimple{
        private Long id;
        private String name;

        public EducationSimple(Education education){
            id = education.getId();
            name = education.getName();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Request {

        @NotNull
        private String name;

        @NotNull
        private String statusName;

        @NotNull
        private Long educationId;

        @NotNull
        private Long branchId;

        private String major;

        public Request(String name, MilitaryServiceStatus status, Education education,
                       Branch branch, String major){
            this.name = name;
            statusName = status.getName();
            educationId = education.getId();
            branchId = branch.getId();
            this.major = major;
        }
        public MilitaryServiceStatus getStatus(){
            return MilitaryServiceStatus.findByName(statusName);
        }

        public void print(){

        }
    }
}
