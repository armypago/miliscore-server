package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetail;
import com.armypago.miliscoreserver.branch.dto.BranchSimple;
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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.stream.Stream;

import static com.armypago.miliscoreserver.branch.BranchApi.BRANCH_URL;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BranchApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired CateogryRepository cateogryRepository;
    @Autowired BranchRepository branchRepository;
    @Autowired EducationRepository educationRepository;
    @Autowired EvaluationRepository evaluationRepository;
    @Autowired UserRepository userRepository;

    @AfterEach
    void tearDown() {
        evaluationRepository.deleteAll();
        userRepository.deleteAll();
        branchRepository.deleteAll();
        cateogryRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("병과 생성")
    void createBranch() throws Exception {
        String name = "SW 개발병";
        Category category = getCategory();
        BranchDetail.Request request = new BranchDetail.Request(category.getId(), name);

        ResultActions result = mockMvc.perform(post(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        BranchDetail.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                BranchDetail.Response.class);

        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("병과 생성 - 유저 권한 시도")
    void createBranchByUser() throws Exception {
        String name = "SW 개발병";
        Category category = getCategory();
        BranchDetail.Request request = new BranchDetail.Request(category.getId(), name);

        mockMvc.perform(post(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("병과 생성 - 중복 이름 병과")
    void createDuplicatedBranch() throws Exception {
        String name = "SW 개발병";
        Category category = getCategory();
        branchRepository.save(Branch.builder().name(name).category(category).build());

        BranchDetail.Request request = new BranchDetail.Request(category.getId(), name);

        mockMvc.perform(post(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이미 존재하는 병과명입니다."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("병과 수정")
    void updateBranch() throws Exception {
        Category category = getCategory();
        Branch branch = branchRepository.save(Branch.builder()
                .name("SW 개발병").category(category).build());

        String changeName = "군사과학기술병";
        BranchDetail.Request request = new BranchDetail.Request(category.getId(), changeName);

        String url = BRANCH_URL + "/" + branch.getId();
        // TODO 수정 기능 접근 불가 (Put Method)
        ResultActions result = mockMvc.perform(put(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        BranchDetail.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                BranchDetail.Response.class);

        assertThat(response.getName()).isEqualTo(changeName);
    }

    @Test
    @DisplayName("전체 병과 조회")
    void readAllBranch() throws Exception {
        Category category = getCategory();
        branchRepository.saveAll(Stream.of("SW 개발병", "군사 과학 기술병", "CERT")
                .map(name -> Branch.builder().name(name).category(category).build())
                .collect(toList()));

        ResultActions result = mockMvc.perform(get(BRANCH_URL))
                .andExpect(status().isOk());

        List<BranchSimple> response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                new TypeReference<List<BranchSimple>>(){});

        boolean hasBranch = (int) response.stream()
                .filter(b -> b.getName().equals("SW 개발병")).count() > 0;

        assertThat(hasBranch).isTrue();
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("병과 조회")
    void readBranch() throws Exception {
        String name = "SW 개발병";
        Category category = getCategory();
        Branch branch = branchRepository.save(Branch.builder()
                .name(name).category(category).build());
        String url = BRANCH_URL + "/" + branch.getId();

        ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        BranchDetail.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                BranchDetail.Response.class);

        assertThat(response.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("병과 조회 - 평가 존재")
    void readBranchWithEvaluations() throws Exception {
        String name = "SW 개발병";
        Category category = getCategory();
        Branch branch = branchRepository.save(Branch.builder()
                .name(name).category(category).build());
        List<Evaluation> evaluations = getEvaluations(branch);

        String url = BRANCH_URL + "/" + branch.getId();

        ResultActions result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        BranchDetail.Response response = new ObjectMapper().readValue(
                result.andReturn().getResponse().getContentAsString(),
                BranchDetail.Response.class);

        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getEvaluations().size()).isEqualTo(evaluations.size());
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

    private List<Evaluation> getEvaluations(Branch branch){
        List<Evaluation> evaluations = getUsers(branch).stream().map(user ->
                Evaluation.builder()
                        .content("개꿀입니다!").score(getScore(new double[]{1, 1, 1, 1, 1, 60}))
                        .branch(branch).author(user).build()).collect(toList());
        return evaluationRepository.saveAll(evaluations);
    }

    private List<User> getUsers(Branch branch){
        Education education = educationRepository.findByPriority(3)
                .orElseGet(()->educationRepository.save(Education.builder()
                        .priority(3).name("대졸").build()));
        List<User> users = Stream.of("aaa", "bbb", "ccc").map(name ->
                User.builder().name(name).email(name + "@gmail.com")
                        .major("software").status(MilitaryServiceStatus.SERVING)
                        .branch(branch).education(education).build()).collect(toList());
        return userRepository.saveAll(users);
    }

    private Category getCategory(){
        return cateogryRepository.save(Category.builder().name("정보통신").build());
    }
}