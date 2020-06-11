package com.armypago.miliscoreserver.domain.user;

import com.armypago.miliscoreserver.user.EducationRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EducationRepository educationRepository;

    @Test
    void createAndUpdateUser(){
        Education education = getEducation();
        User user = userRepository.save(getUser(education));

        userRepository.findByEmail(user.getEmail()).ifPresent(byEmail -> {
            assertThat(byEmail).isNotNull();
            assertThat(byEmail.getRole()).isEqualTo(Role.USER);
            assertThat(byEmail.getStatus()).isEqualTo(MilitaryServiceStatus.SERVING);
            assertThat(byEmail.getEducation().getId()).isEqualTo(education.getId());
        });
    }

    private Education getEducation(){
        return educationRepository.save(Education.builder().priority(3).name("대졸").build());
    }

    private User getUser(Education education){
        return User.builder()
                .name("junyoung").email("cupjoo@gmail.com")
                .major("software").status(MilitaryServiceStatus.SERVING)
                .education(education).build();
    }
}