package com.armypago.miliscoreserver.domain.branch;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

// TODO toString, EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
@Entity
public class Branch extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "branch")
    private List<Evaluation> evaluations = new ArrayList();

    @Builder
    public Branch(String name){
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
