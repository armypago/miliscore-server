package com.armypago.miliscoreserver.domain.user;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    private Role role;

    @Column(updatable = false)
    private String major;

    @Column(name = "military_service_status")
    private MilitaryServiceStatus status;

    @Column(updatable = false)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "education_id")
    private Education education;

    // TODO : 인증여부, 입대년도, ...

    @Builder
    public User(String name, String email, String major,
                MilitaryServiceStatus status, Education education){
        role = Role.USER;
        this.name = name;
        this.email = email;
        this.major = major;
        this.status = status;
        this.education = education;
    }

    public void updateInfo(String email, MilitaryServiceStatus status){
        this.email = email;
        this.status = status;
    }
}
