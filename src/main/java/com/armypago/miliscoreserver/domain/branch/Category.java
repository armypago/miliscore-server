package com.armypago.miliscoreserver.domain.branch;

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
public class Category {

    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Builder
    public Category(String name){
        this.name = name;
    }
}
