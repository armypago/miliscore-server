package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class CategoryDetail {

    // TODO 제약조건 추가
    // TODO LocalDateTime 파싱

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Response {
        private Long id;
        private String name;
//        private LocalDateTime modifiedDate;
        private List<BranchSimple> branches = new ArrayList<>();

        public Response(Category category, List<BranchSimple> branches){
            id = category.getId();
            name = category.getName();
//            modifiedDate = category.getModifiedDate();
            if(branches != null && !branches.isEmpty()){
                this.branches = branches;
            }
        }
    }

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Request {

        @NotNull
        private String name;

        public Request(String name){
            this.name = name;
        }

        public Category toEntity(){
            return Category.builder().name(name).build();
        }
    }
}
