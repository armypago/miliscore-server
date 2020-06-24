package com.armypago.miliscoreserver.evaluation.dto;

import com.armypago.miliscoreserver.config.auth.validator.InconsistentUser;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.validator.DuplicateAuthor;
import lombok.*;

import java.time.LocalDateTime;

public class EvaluationDetailDto {

    // TODO 제약 조건 추가

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Response {

        private Long id;
        private BranchDto branch;
        private UserDto user;
        private String content;
        private RadarChart score;
        private LocalDateTime modifiedDate;

        public Response(Evaluation evaluation){
            branch = new BranchDto(evaluation.getBranch());
            user = new UserDto(evaluation.getAuthor());
            content = evaluation.getContent();
            score = evaluation.getScore();
            modifiedDate = evaluation.getModifiedDate();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Request {

        @DuplicateAuthor
        private EvaluationKey key;

        @InconsistentUser
        private Long authorId;

        private Long branchId;
        private String content;
        private RadarChart score;

        public Request(User author, Branch branch, String content, RadarChart score){
            authorId = author.getId();
            branchId = branch.getId();
            key = new EvaluationKey(authorId, branchId);
            this.content = content;
            this.score = score;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EvaluationKey {
        private Long authorId;
        private Long branchId;
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class BranchDto {
        private Long id;
        private String name;

        public BranchDto(Branch branch){
            id = branch.getId();
            name = branch.getName();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class UserDto {
        private Long id;
        private String name;

        public UserDto(User user){
            id = user.getId();
            name = user.getName();
        }
    }
}
