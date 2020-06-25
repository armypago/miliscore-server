package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.WithUser;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetailDto;
import com.armypago.miliscoreserver.user.EducationRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.armypago.miliscoreserver.evaluation.EvaluationApi.EVALUATION_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class EvaluationApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired EvaluationRepository evaluationRepository;
    @Autowired EducationRepository educationRepository;
    @Autowired EntityManager em;

    @AfterEach
    void tearDown() {
        evaluationRepository.deleteAll();
    }

    final String userName = "test";
    final String userEmail = "test@gmail.com";

    @Test
    @WithUser(name = userName, email = userEmail, status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 생성")
    void createEvaluation() throws Exception {
        Branch branch = getBranch();
        User user = getUser(branch);

        String content = "개꿀입니다!";
        RadarChart score = getScore(new double[]{1,1,1,1,1,60});

        EvaluationDetailDto.Request request
                = new EvaluationDetailDto.Request(user, branch, content, score);

        mockMvc.perform(post(EVALUATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        Evaluation evaluation = evaluationRepository.findByBranchId(branch.getId()).get(0);
        assertThat(evaluation.getContent()).isEqualTo(content);
    }

    @Test
    @WithUser(name = userName+"a", email = userEmail+"b", status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 생성 - 로그인 정보 불일치")
    void createEvaluationWithInconsistentUser() {
        Branch branch = getBranch();
        User user = getUser(branch);

        String content = "개꿀입니다!";
        RadarChart score = getScore(new double[]{1,1,1,1,1,60});

        EvaluationDetailDto.Request request
                = new EvaluationDetailDto.Request(user, branch, content, score);

        assertThatThrownBy(
                () -> mockMvc.perform(post(EVALUATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))))
                .hasCause(new IllegalArgumentException("로그인 정보가 일치하지 않습니다."));
    }

    @Test
    @WithUser(name = userName, email = userEmail, status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 생성 - 평가 2회 이상")
    void createDuplicateEvaluation() {
        Branch branch = getBranch();
        User user = getUser(branch);

        String content = "개꿀입니다!";
        RadarChart score = getScore(new double[]{1,1,1,1,1,60});

        Evaluation evaluation = Evaluation.builder()
                .content(content).score(score)
                .branch(branch).author(user).build();
        evaluationRepository.save(evaluation);

        EvaluationDetailDto.Request request
                = new EvaluationDetailDto.Request(user, branch, content, score);
        assertThatThrownBy(
                () -> mockMvc.perform(post(EVALUATION_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))))
                .hasCause(new IllegalArgumentException("이미 평가를 작성한 사용자입니다."));
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
                .name(userName).email(userEmail)
                .major("software").status(MilitaryServiceStatus.SERVING)
                .branch(branch).education(education).build();
        return userRepository.findByEmail(user.getEmail())
                .orElseGet(()->userRepository.save(user));
    }
}