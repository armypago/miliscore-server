package com.armypago.miliscoreserver.domain.evaluation;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Evaluation extends BaseTimeEntity {

    // TODO 익명 여부
    
    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(nullable = false)
    private String content;

    private String description;

    @Embedded
    private RadarChart score;

    // TODO 조회수 기능

    @Builder
    public Evaluation(Branch branch, User author, String content,
                      RadarChart score, String description){
        this.branch = branch;
        this.author = author;
        this.content = content;
        this.score = score;
        this.description = description;
        this.branch.addEvaluation(this);
    }

    public void updateInfo(String content, RadarChart score, String description){
        this.content = content;
        this.score = score;
        this.description = description;
    }
}
