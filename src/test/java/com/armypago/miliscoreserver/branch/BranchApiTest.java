package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetailDto;
import com.armypago.miliscoreserver.branch.dto.BranchListDto;
import com.armypago.miliscoreserver.domain.branch.Branch;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.armypago.miliscoreserver.branch.BranchApi.BRANCH_URL;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BranchApiTest {

    @Autowired MockMvc mockMvc;
    @Autowired BranchRepository branchRepository;

    @AfterEach
    void tearDown() {
        branchRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("병과 생성")
    void createBranch() throws Exception {
        BranchDetailDto.Request request = new BranchDetailDto.Request();
        String name = "SW 개발병";
        request.setName(name);

        mockMvc.perform(post(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        branchRepository.findByName(name).ifPresent(b ->
                assertThat(b.getName()).isEqualTo(name));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("병과 생성 - 유저 권한 시도")
    void createBranchByUser() throws Exception {
        BranchDetailDto.Request request = new BranchDetailDto.Request();
        String name = "SW 개발병";
        request.setName(name);

        mockMvc.perform(post(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("병과 생성 - 중복 이름 병과")
    void createDuplicatedBranch() {
        String name = "SW 개발병";
        branchRepository.save(Branch.builder().name(name).build());

        BranchDetailDto.Request request = new BranchDetailDto.Request();
        request.setName(name);

        assertThatThrownBy(
                () -> mockMvc.perform(post(BRANCH_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))))
                .hasCause(new IllegalArgumentException("이미 존재하는 병과명입니다."));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    @DisplayName("병과 수정")
    void updateBranch() throws Exception {
        String changeName = "군사과학기술병";
        Long id = branchRepository.save(Branch.builder().name("SW 개발병").build()).getId();
        BranchDetailDto.Request request = new BranchDetailDto.Request();
        request.setName(changeName);
        String url = BRANCH_URL + "/" + id;

        // TODO 수정 기능 접근 불가 (Put Method)
        mockMvc.perform(put(BRANCH_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        branchRepository.findByName(changeName).ifPresent(b ->
                assertThat(b.getName()).isEqualTo(changeName));
    }

    @Test
    @DisplayName("전체 병과 조회")
    void readAllBranch() throws Exception {
        branchRepository.saveAll(Stream.of("SW 개발병", "군사 과학 기술병", "CERT")
                .map(Branch::new).collect(toList()));

        String content = mockMvc.perform(get(BRANCH_URL))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BranchListDto> response = new ObjectMapper()
                .readValue(content, new TypeReference<List<BranchListDto>>(){});
        boolean hasBranch = (int) response.stream()
                .filter(b -> b.getName().equals("SW 개발병")).count() > 0;

        assertThat(hasBranch).isTrue();
        assertThat(response.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("병과 조회")
    void readBranch() throws Exception {
        String name = "SW 개발병";
        Branch branch = branchRepository.save(Branch.builder().name(name).build());
        BranchDetailDto.Request request = new BranchDetailDto.Request(branch);
        String url = BRANCH_URL + "/" + branch.getId();

        String content = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        BranchDetailDto.Response response = new ObjectMapper()
                .readValue(content, BranchDetailDto.Response.class);

        assertThat(response.getName()).isEqualTo(name);
    }
}