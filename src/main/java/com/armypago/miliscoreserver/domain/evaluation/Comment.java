package com.armypago.miliscoreserver.domain.evaluation;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
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
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Evaluation evaluation, User author, String content){
        this.evaluation = evaluation;
        this.author = author;
        this.content = content;
    }

    public void changeContent(String content){
        this.content = content;
    }
}
