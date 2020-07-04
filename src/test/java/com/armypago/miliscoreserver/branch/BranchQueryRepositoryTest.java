package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetail;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.branch.Category;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.EvaluationRepository;
import com.armypago.miliscoreserver.user.EducationRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BranchQueryRepositoryTest {

    @Autowired BranchQueryRepository branchQueryRepository;
    @Autowired BranchRepository branchRepository;
    @Autowired EvaluationRepository evaluationRepository;
    @Autowired UserRepository userRepository;
    @Autowired EducationRepository educationRepository;
    @Autowired CateogryRepository cateogryRepository;

    private Branch branch;
    private final String content = "꼭 지원하세요!";
    private final double avgDayOfLeaves = 50;

    @AfterEach
    void tearDown() {
        evaluationRepository.deleteAll();
        branchRepository.deleteAll();
        userRepository.deleteAll();
        cateogryRepository.deleteAll();
    }

    @BeforeEach
    void before(){
        Category category = cateogryRepository.save(Category.builder().name("정보통신").build());
        branch = branchRepository.save(Branch.builder().name("SW 개발병").category(category).build());
        User user = getUser(branch);
        getEvaluation(branch, user, getScore(new double[]{1, 2, 3, 4, 5, avgDayOfLeaves+10}));
        getEvaluation(branch, user, getScore(new double[]{3, 4, 5, 6, 7, avgDayOfLeaves-10}));
    }

    @Test
    @DisplayName("병과 상세 조회")
    void getBranch(){
        BranchDetail.Response branchResponse = branchQueryRepository.findById(branch.getId());
        List<BranchDetail.EvaluationSimple> evaluations = branchResponse.getEvaluations();
        assertThat(evaluations.size()).isEqualTo(2);
        assertThat(evaluations.stream().anyMatch(e -> e.getContent().equals(content))).isTrue();
        assertThat(branchResponse.getScore().getDayOfLeaves()).isEqualTo(avgDayOfLeaves);
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

    private void getEvaluation(Branch branch, User user, RadarChart score) {
        evaluationRepository.save(Evaluation.builder()
                .content(content).branch(branch).author(user).score(score).build());
    }

    private User getUser(Branch branch){
        Education education = educationRepository.findByPriority(3)
                .orElseGet(()->educationRepository.save(Education.builder()
                        .priority(3).name("대졸").build()));
        return userRepository.save(User.builder()
                .education(education).branch(branch)
                .status(MilitaryServiceStatus.SERVING)
                .name("Junyoung").major("Software").email("cupjoo@gmail.com").build());
    }
}