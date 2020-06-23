package com.armypago.miliscoreserver.domain.user;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import com.armypago.miliscoreserver.domain.branch.Branch;
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

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    private String major;

    @Enumerated(EnumType.STRING)
    @Column(name = "military_service_status")
    private MilitaryServiceStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "education_id")
    private Education education;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    // TODO : 인증여부, 입대년도, ...

    @Builder
    public User(String name, String email, String major,
                MilitaryServiceStatus status, Education education, Branch branch){
        role = Role.USER;
        this.name = name;
        this.email = email;
        this.major = major;
        this.status = status;
        this.education = education;
        this.branch = branch;
    }

    public void updateInfo(String email, MilitaryServiceStatus status){
        this.email = email;
        this.status = status;
    }

    public String getRoleKey() {
        return role.getKey();
    }

    public void changeRole(Role role){
        this.role = role;
    }
}
