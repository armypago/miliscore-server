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

    @Lob
    @Column(unique = true)
    private String token;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String major = null;

    @Enumerated(EnumType.STRING)
    @Column(name = "military_service_status")
    private MilitaryServiceStatus status = null;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "education_id")
    private Education education = null;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch = null;

    private boolean authenticated = false;

    // TODO : 입대년도, ...

//    @Builder
//    public User(String name, String email, String major,
//                MilitaryServiceStatus status, Education education, Branch branch){
//        role = Role.USER;
//        this.name = name;
//        this.email = email;
//        initialize(status, education, branch, major);
//    }

    @Builder
    public User(String token){
        this.token = token;
        role = Role.USER;
    }

    public boolean initialize(String name, String email, MilitaryServiceStatus status,
                              Education education, Branch branch, String major){
        if (hasInitialized()) {
            return false;
        }
        this.name = name;
        this.email = email;
        this.status = status;
        this.education = education;
        this.branch = branch;
        this.major = major;
        return true;
    }

    public boolean hasInitialized() {
        return email != null || name != null ||
                status != null || education != null ||
                branch != null || major != null;
    }

    public void updateStatus(MilitaryServiceStatus status){
        this.status = status;
    }

    public String getRoleKey() {
        return role.getKey();
    }

    public void changeRole(Role role){
        this.role = role;
    }

    public void authenicate(){
        authenticated = true;
    }
}
