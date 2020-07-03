package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetail;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.branch.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BranchService {

    private final CateogryRepository cateogryRepository;
    private final BranchRepository branchRepository;

    public BranchDetail.Response createBrunch(Long categoryId, String name){
        Optional<Category> category = cateogryRepository.findById(categoryId);
        if(!category.isPresent()){
            return null;
        }
        Branch branch = Branch.builder().category(category.get()).name(name).build();
        return new BranchDetail.Response(branchRepository.save(branch), null);
    }
}
