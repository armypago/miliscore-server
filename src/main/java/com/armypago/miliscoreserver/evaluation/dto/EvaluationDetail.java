package com.armypago.miliscoreserver.evaluation.dto;

import com.armypago.miliscoreserver.config.auth.validator.InconsistentUser;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.validator.DuplicateAuthor;
import lombok.*;

public class EvaluationDetail {

    // TODO 제약 조건 추가
    // TODO LocalDateTime 파싱

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Response {

        private Long id;
        private BranchSimple branch;
        private UserSimple author;
        private String content;
        private RadarChart score;
//        private LocalDateTime modifiedDate;

        public Response(Evaluation evaluation){
            id = evaluation.getId();
            branch = new BranchSimple(evaluation.getBranch());
            author = new UserSimple(evaluation.getAuthor());
            content = evaluation.getContent();
            score = evaluation.getScore();
//            modifiedDate = evaluation.getModifiedDate();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    @DuplicateAuthor
    public static class Request {

        @InconsistentUser
        private Long authorId;

        private Long branchId;
        private String content;
        private RadarChart score;

        public Request(User author, Branch branch, String content, RadarChart score){
            authorId = author.getId();
            branchId = branch.getId();
            this.content = content;
            this.score = score;
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class BranchSimple {
        private Long id;
        private String name;

        public BranchSimple(Branch branch){
            id = branch.getId();
            name = branch.getName();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class UserSimple {
        private Long id;
        private String name;

        public UserSimple(User user){
            id = user.getId();
            name = user.getName();
        }
    }
}
