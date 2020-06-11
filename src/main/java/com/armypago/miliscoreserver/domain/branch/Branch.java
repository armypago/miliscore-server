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
public class Branch {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @Builder
    public Branch(String name){
        this.name = name;
    }

    public void changeInfo(String name){
        this.name = name;
    }
}
