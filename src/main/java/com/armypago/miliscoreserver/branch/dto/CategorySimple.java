package com.armypago.miliscoreserver.branch.dto;

import com.armypago.miliscoreserver.domain.branch.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class CategorySimple {

    private Long id;
    private String name;

    public CategorySimple(Category category){
        id = category.getId();
        name = category.getName();
    }
}
