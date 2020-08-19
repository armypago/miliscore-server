package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.CategoryDetail;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.branch.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class CategoryQueryRepositoryTest {

    @Autowired CategoryQueryRepository categoryQueryRepository;
    @Autowired CateogryRepository cateogryRepository;
    @Autowired BranchRepository branchRepository;

    @Test
    void select(){
        Category category = cateogryRepository.save(Category.builder().name("정보통신").build());

        List<Branch> branches = Stream.of("SW 개발병", "CERT")
                .map(name -> Branch.builder().category(category).name(name).build())
                .collect(toList());
        branchRepository.saveAll(branches);

        CategoryDetail.Response response = categoryQueryRepository.findById(category.getId());
        assertThat(response.getBranches().size()).isEqualTo(branches.size());
    }
}