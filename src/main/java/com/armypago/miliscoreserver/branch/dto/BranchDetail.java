package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.branch.validator.UniqueName;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.branch.Category;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class BranchDetail {

    // TODO 제약조건 추가
    // TODO LocalDateTime 파싱

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Response {
        private Long id;
        private String name;
//        private LocalDateTime modifiedDate;

        private RadarChart score;
        private CategorySimple category;
        private List<EvaluationSimple> evaluations = new ArrayList<>();
        private List<LocalDate> recruits = new ArrayList<>();

        public Response(Branch branch, RadarChart score, List<LocalDate> recruits){
            id = branch.getId();
            name = branch.getName();
//            modifiedDate = branch.getModifiedDate();
            evaluations = branch.getEvaluations().stream()
                    .map(EvaluationSimple::new).collect(toList());
            this.score = score;
            category = new CategorySimple(branch.getCategory());
            if(recruits != null){
                this.recruits = recruits;
            }
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Request {

        private Long categoryId;

        @UniqueName
        @NotNull
        private String name;

        private String description;

        public Request(Long categoryId, String name, String description){
            this.categoryId = categoryId;
            this.name = name;
            this.description = description;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class EvaluationSimple {

        private Long id;

        @Column(nullable = false)
        private String content;

        public EvaluationSimple(Evaluation evaluation){
            id = evaluation.getId();
            content = evaluation.getContent();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class CategorySimple {

        private Long id;

        private String name;

        public CategorySimple(Category category){
            id = category.getId();
            name = category.getName();
        }
    }
}
