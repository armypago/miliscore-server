package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BranchDetailDto {

    // TODO 제약조건 추가

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Response {
        private Long id;
        private String name;
        private LocalDateTime modifiedDate;

        private RadarChart score;
        private List<EvaluationListDto> evaluations;

        public Response(Branch branch, RadarChart score){
            id = branch.getId();
            name = branch.getName();
            modifiedDate = branch.getModifiedDate();
            evaluations = branch.getEvaluations().stream()
                    .map(EvaluationListDto::new).collect(toList());
            this.score = score;
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Request {
        private String name;

        public Request(Branch branch){
            name = branch.getName();
        }

        public Branch toEntity(){
            return Branch.builder().name(name).build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class EvaluationListDto {

        private Long id;

        @Column(nullable = false)
        private String content;

        public EvaluationListDto(Evaluation evaluation){
            id = evaluation.getId();
            content = evaluation.getContent();
        }
    }
}