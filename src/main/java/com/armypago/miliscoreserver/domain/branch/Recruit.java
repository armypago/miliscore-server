package com.armypago.miliscoreserver.domain.branch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Recruit {

    @Id
    @GeneratedValue(strategy= IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Branch branch;

    @Column(updatable = false)
    private LocalDate date;

    @Builder
    public Recruit(Branch branch, LocalDate date){
        this.branch = branch;
        this.date = date;
    }
}
