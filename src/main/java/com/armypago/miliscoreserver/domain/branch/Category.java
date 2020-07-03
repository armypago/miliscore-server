package com.armypago.miliscoreserver.domain.branch;

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
public class Category {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @Builder
    public Category(String name){
        this.name = name;
    }
}
