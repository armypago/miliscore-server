package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.WithUser;
import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.branch.dto.BranchListDto;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetailDto;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationUpdateDto;
import com.armypago.miliscoreserver.user.EducationRepository;
import com.armypago.miliscoreserver.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import jdk.internal.org.objectweb.asm.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.armypago.miliscoreserver.evaluation.EvaluationApi.EVALUATION_URL;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class EvaluationApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired EvaluationRepository evaluationRepository;
    @Autowired EducationRepository educationRepository;
    @Autowired BranchRepository branchRepository;
    @Autowired EntityManager em;

    @AfterEach
    void tearDown() {
        evaluationRepository.deleteAll();
        userRepository.deleteAll();
        branchRepository.deleteAll();
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

        ResultActions result = mockMvc.perform(post(EVALUATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        EvaluationDetailDto.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                EvaluationDetailDto.Response.class);

        assertThat(response.getContent()).isEqualTo(content);
    }

    @Test
    @WithUser(name = userName+"a", email = userEmail+"b", status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 생성 - 로그인 정보 불일치")
    void createEvaluationWithInconsistentUser() throws Exception {
        Branch branch = getBranch();
        User user = getUser(branch);

        String content = "개꿀입니다!";
        RadarChart score = getScore(new double[]{1,1,1,1,1,60});

        EvaluationDetailDto.Request request
                = new EvaluationDetailDto.Request(user, branch, content, score);

        mockMvc.perform(post(EVALUATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("로그인 정보가 일치하지 않습니다."));
    }

    @Test
    @WithUser(name = userName, email = userEmail, status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 생성 - 평가 2회 이상")
    void createDuplicateEvaluation() throws Exception {
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

        mockMvc.perform(post(EVALUATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이미 평가를 작성한 사용자입니다."));
    }

    @Test
    @WithUser(name = userName, email = userEmail, status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 수정")
    void updateEvaluation() throws Exception {
        Branch branch = getBranch();
        User user = getUser(branch);

        Evaluation evaluation = Evaluation.builder()
                .content("개꿀입니다!").score(getScore(new double[]{1,1,1,1,1,60}))
                .branch(branch).author(user).build();
        evaluationRepository.save(evaluation);

        String updateContent = "낄낄 난 전역한다!";
        RadarChart updateScore = getScore(new double[]{5,5,5,5,5,30});

        EvaluationUpdateDto.Request request
                = new EvaluationUpdateDto.Request(updateContent, updateScore);

        String url = EVALUATION_URL + "/" + evaluation.getId();

        ResultActions result = mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        EvaluationDetailDto.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                EvaluationDetailDto.Response.class);

        assertThat(response.getContent()).isEqualTo(updateContent);
    }

    @Test
    @WithUser(name = userName+"a", email = userEmail+"b", status = MilitaryServiceStatus.SERVING)
    @DisplayName("평가 수정 - 작성자 불일치")
    void updateEvaluationWithInconsistentAuthor() throws Exception {
        Branch branch = getBranch();
        User user = getUser(branch);

        Evaluation evaluation = Evaluation.builder()
                .content("개꿀입니다!").score(getScore(new double[]{1,1,1,1,1,60}))
                .branch(branch).author(user).build();
        evaluationRepository.save(evaluation);

        String updateContent = "낄낄 난 전역한다!";
        RadarChart updateScore = getScore(new double[]{5,5,5,5,5,30});

        EvaluationUpdateDto.Request request
                = new EvaluationUpdateDto.Request(updateContent, updateScore);

        String url = EVALUATION_URL + "/" + evaluation.getId();

        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("작성자 정보가 일치하지 않습니다."));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("평가 조회")
    void readEvaluation() throws Exception {
        Branch branch = getBranch();
        User user = getUser(branch);

        Evaluation evaluation = Evaluation.builder()
                .content("개꿀입니다!").score(getScore(new double[]{1,1,1,1,1,60}))
                .branch(branch).author(user).build();
        evaluationRepository.save(evaluation);

        String url = EVALUATION_URL + "/" + evaluation.getId();

        ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        EvaluationDetailDto.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                EvaluationDetailDto.Response.class);

        assertThat(response.getAuthor().getId()).isEqualTo(user.getId());
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
        return branchRepository.save(Branch.builder().name("SW개발병").build());
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