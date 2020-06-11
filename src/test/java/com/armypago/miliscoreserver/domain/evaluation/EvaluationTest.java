package com.armypago.miliscoreserver.domain.evaluation;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.EvaluationRepository;
import com.armypago.miliscoreserver.user.EducationRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class EvaluationTest {

    @Autowired BranchRepository branchRepository;
    @Autowired UserRepository userRepository;
    @Autowired EducationRepository educationRepository;
    @Autowired EvaluationRepository evaluationRepository;
    
    @Test
    @DisplayName("한 줄 평가 생성")
    void createAndUpdateEvaluation(){
        Branch branch = getBranch();
        User user = getUser(branch);
        String content = "개꿀입니다!";
        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .author(user).branch(branch).content(content).build());
        evaluationRepository.findById(evaluation.getId()).ifPresent(e -> {
            assertThat(e.getAuthor().getName()).isEqualTo(user.getName());
            assertThat(e.getBranch().getName()).isEqualTo(branch.getName());
            assertThat(e.getContent()).isEqualTo(content);
        });
    }

    private Branch getBranch() {
        return branchRepository.save(Branch.builder().name("SW개발병").build());
    }

    private User getUser(Branch branch) {
        Education education = educationRepository.save(Education.builder().priority(3).name("대졸").build());
        return userRepository.save(User.builder()
                .name("junyoung").email("cupjoo@gmail.com")
                .major("software").status(MilitaryServiceStatus.SERVING)
                .branch(branch).education(education).build());
    }
}