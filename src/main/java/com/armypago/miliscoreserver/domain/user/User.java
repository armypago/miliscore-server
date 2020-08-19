package com.armypago.miliscoreserver.domain.user;

import com.armypago.miliscoreserver.domain.BaseTimeEntity;
import com.armypago.miliscoreserver.domain.branch.Branch;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy= IDENTITY)
    private Long id;

    private String name = null;

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

    private String serialNumber = null;
    // TODO unique 조건 조금 비교

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch = null;

    private boolean authenticated = false;

//    @Builder
//    public User(String name, String email, String major,
//                MilitaryServiceStatus status, Education education, Branch branch){
//        role = Role.USER;
//        this.name = name;
//        this.email = email;
//        initialize(status, education, branch, major);
//    }

    @Builder
    public User(String email){
        this.email = email;
        role = Role.USER;
    }

    public boolean initialize(String name, MilitaryServiceStatus status, String serialNumber,
                              Education education, Branch branch, String major){
        if (hasInitialized()) {
            return false;
        }
        this.name = name;
        this.status = status;
        this.education = education;
        this.serialNumber = serialNumber;
        this.branch = branch;
        this.major = major;
        return true;
    }

    public boolean hasInitialized() {
        return name != null || serialNumber != null ||
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
