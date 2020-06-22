package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.user.EducationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class EvaluationRepositoryTest {

    @Autowired EducationRepository educationRepository;
    @Autowired EvaluationRepository evaluationRepository;
    @Autowired EntityManager em;

    @Test
    @DisplayName("평가 생성/조회")
    void createAndUpdateEvaluation(){
        Branch branch = getBranch();
        User user = getUser(branch);
        String content = "개꿀입니다!";
        RadarChart score = getScore(new double[]{1,1,1,1,1,60});
        
        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .score(score).author(user).branch(branch).content(content).build());
        evaluationRepository.findById(evaluation.getId()).ifPresent(e -> {
            assertThat(e.getAuthor().getName()).isEqualTo(user.getName());
            assertThat(e.getBranch().getName()).isEqualTo(branch.getName());
            assertThat(e.getContent()).isEqualTo(content);
        });
        boolean byAuthorAndBranch = evaluationRepository
                .findByAuthorIdAndBranchId(user.getId(), branch.getId())
                .isPresent();
        assertThat(byAuthorAndBranch).isTrue();
    }

    private RadarChart getScore(double[] score){
        return RadarChart.builder()
                .careerRelevance(score[0])
                .workLifeBalance(score[1])
                .unitVibe(score[2])
                .trainingIntensity(score[3])
                .officer(score[4])
                .dayOfLeaves(score[5]).build();
    }

    private Branch getBranch() {
        Branch branch = Branch.builder().name("SW개발병").build();
        em.persist(branch);
        return branch;
    }

    private User getUser(Branch branch) {
        Education education = educationRepository.findByPriority(3)
                .orElseGet(()->educationRepository.save(Education.builder()
                        .priority(3).name("대졸").build()));
        User user = User.builder()
                .name("junyoung").email("cupjoo@gmail.com")
                .major("software").status(MilitaryServiceStatus.SERVING)
                .branch(branch).education(education).build();
        em.persist(user);
        return user;
    }
}