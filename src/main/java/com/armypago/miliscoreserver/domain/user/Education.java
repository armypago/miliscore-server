package com.armypago.miliscoreserver.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Education {

    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @Column(unique = true)
    private int priority;

    @Column(nullable = false)
    private String name;

    @Builder
    public Education(int priority, String name){
        this.priority = priority;
        this.name = name;
    }
}
