package com.armypago.miliscoreserver.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Education {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private int order;

    // TODO: 고졸, 대재, 대졸, 휴학, 석재, 석졸
    @Column(nullable = false)
    private String name;

    @Builder
    public Education(int order, String name){
        this.order = order;
        this.name = name;
    }
}
