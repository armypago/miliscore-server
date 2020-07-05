package com.armypago.miliscoreserver.domain.branch;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

// TODO toString, EqualsAndHashCode(of = "id")
@Getter
@NoArgsConstructor
@Entity
public class Branch extends BaseTimeEntity {

    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = LAZY)
    private Category category;

    @OneToMany(mappedBy = "branch")
    private List<Evaluation> evaluations = new ArrayList();

    private String description;

    @Builder
    public Branch(Category category, String name, String description){
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public void changeInfo(String name, String description){
        this.name = name;
        this.description = description;
    }

    // TODO remove eval

    public void addEvaluation(Evaluation evaluation){
        evaluations.add(evaluation);
    }
}
