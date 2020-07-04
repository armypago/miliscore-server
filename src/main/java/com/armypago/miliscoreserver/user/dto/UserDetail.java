package com.armypago.miliscoreserver.user.dto;

import com.armypago.miliscoreserver.branch.dto.BranchSimple;
import com.armypago.miliscoreserver.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDetail {

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Response {

        private Long id;
        private String name;
        private String email;
        private String major;
        private String status;
        private UserCreate.EducationSimple education;
        private BranchSimple branch;

        public Response(User user){
            id = user.getId();
            name = user.getName();
            email = user.getEmail();
            major = user.getMajor();
            status = user.getStatus().getName();
            education = new UserCreate.EducationSimple(user.getEducation());
            branch = new BranchSimple(user.getBranch());
        }
    }
}
