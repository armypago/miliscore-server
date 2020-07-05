package com.armypago.miliscoreserver.evaluation.dto;

import com.armypago.miliscoreserver.config.auth.validator.InconsistentUser;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.Interview;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.validator.DuplicateAuthor;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
        private String description;
        private List<InterviewSimple> interviews = new ArrayList<>();
//        private LocalDateTime modifiedDate;

        public Response(Evaluation evaluation, List<Interview> interviews){
            id = evaluation.getId();
            branch = new BranchSimple(evaluation.getBranch());
            author = new UserSimple(evaluation.getAuthor());
            content = evaluation.getContent();
            score = evaluation.getScore();
            description = evaluation.getDescription();
            if(interviews != null && !interviews.isEmpty()){
                this.interviews = interviews.stream()
                        .map(InterviewSimple::new).collect(toList());
            }
//            modifiedDate = evaluation.getModifiedDate();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    @DuplicateAuthor
    public static class Request {

//        @InconsistentUser
        private Long authorId;

        private Long branchId;
        private String content;
        private RadarChart score;
        private String description;
        private List<InterviewSimple> interviews = new ArrayList<>();

        public Request(User author, Branch branch, String content,
                       RadarChart score, String description, List<InterviewSimple> interviews){
            authorId = author.getId();
            branchId = branch.getId();
            this.content = content;
            this.score = score;
            this.description = description;
            this.interviews = interviews;
        }

        public List<Interview> getInterviews(Evaluation evaluation){
            return interviews.stream()
                    .map(interview -> interview.toEntity(evaluation))
                    .collect(toList());
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
    public static class InterviewSimple {
        private String question;
        private String answer;

        public InterviewSimple(Interview interview){
            question = interview.getQuestion();
            answer = interview.getAnswer();
        }

        public Interview toEntity(Evaluation evaluation){
            return Interview.builder()
                    .evaluation(evaluation)
                    .question(question).answer(answer).build();
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class UserSimple {
        private Long id;
        private String name;
        private String enrollmentYear;
        private String education;
        private String major;

        public UserSimple(User user){
            id = user.getId();
            name = user.getName();
            enrollmentYear = user.getSerialNumber().substring(0, 2);
            education = user.getEducation().getName();
            major = user.getMajor();
        }
    }
}
