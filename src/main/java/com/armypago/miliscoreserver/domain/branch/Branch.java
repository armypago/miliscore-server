package com.armypago.miliscoreserver.domain.branch;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

// TODO toString, EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
@Entity
public class Branch extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = LAZY)
    private Category category;

    @OneToMany(mappedBy = "branch")
    private List<Evaluation> evaluations = new ArrayList();

    @Builder
    public Branch(Category category, String name){
        this.category = category;
        this.name = name;
    }

    public void changeInfo(String name){
        this.name = name;
    }

    // TODO remove eval

    public void addEvaluation(Evaluation evaluation){
        evaluations.add(evaluation);
    }
}
