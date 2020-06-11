package com.armypago.miliscoreserver.domain.user;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.user.EducationRepository;
import com.armypago.miliscoreserver.user.UserQueryRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserTest {

    @Autowired UserRepository userRepository;
    @Autowired UserQueryRepository userQueryRepository;
    @Autowired EducationRepository educationRepository;
    @Autowired BranchRepository branchRepository;

    @Test
    @DisplayName("사용자 생성")
    void createAndUpdateUser(){
        Education education = getEducation();
        Branch branch = getBranch();
        User user = userRepository.save(getUser(education, branch));

        User byEmail = userQueryRepository.findByEmail(user.getEmail());
        assertThat(byEmail).isNotNull();
        assertThat(byEmail.getRole()).isEqualTo(Role.USER);
        assertThat(byEmail.getStatus()).isEqualTo(MilitaryServiceStatus.SERVING);
        assertThat(byEmail.getEducation().getName()).isEqualTo(education.getName());
        assertThat(byEmail.getBranch().getName()).isEqualTo(branch.getName());
    }

    private Branch getBranch(){
        return branchRepository.save(Branch.builder().name("SW개발병").build());
    }

    private Education getEducation(){
        Optional<Education> byPriority = educationRepository.findByPriority(3);
        return byPriority
                .orElseGet(() -> educationRepository.save(Education.builder()
                        .priority(3).name("대졸").build()));
    }

    private User getUser(Education education, Branch branch){
        return User.builder()
                .name("junyoung").email("cupjoo@gmail.com")
                .major("software").status(MilitaryServiceStatus.SERVING)
                .branch(branch).education(education).build();
    }
}