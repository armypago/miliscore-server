package com.armypago.miliscoreserver.domain.evaluation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Interview {

    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    private String question;

    private String answer;

    @Builder
    public Interview(Evaluation evaluation, String question, String answer){
        this.evaluation = evaluation;
        this.question = question;
        this.answer = answer;
    }
}
