package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDto;
import com.armypago.miliscoreserver.config.auth.LoginUser;
import com.armypago.miliscoreserver.config.auth.dto.SessionUser;
import com.armypago.miliscoreserver.domain.branch.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(BranchApi.BRANCH_URL)
@RestController
public class BranchApi {

    static final String BRANCH_URL = "/api/v1/branch";

    private final BranchRepository branchRepository;

    // TODO : APP 용 오류 메시지, ResponseBody or ResponseEntity 형식으로 변경
    // TODO : 예외 처리 추가
    // TODO : 로그인 인증 상태 확인 (DELETE, UPDATE)
    // TODO : 조건부 검색 추가
    
    @PostMapping
    public Long create(@LoginUser SessionUser user, @Valid @RequestBody BranchDto branchDto){
        branchRepository.findByName(branchDto.getName()).ifPresent(b -> {
            throw new IllegalArgumentException("이미 존재하는 병과입니다. name = "+b.getName());
        });
        return branchRepository.save(branchDto.toEntity()).getId();
    }

    @PutMapping("/{id}")
    public Long update(@LoginUser SessionUser user, @PathVariable Long id,
                       @Valid BranchDto branchDto, Errors errors){
        if(errors.hasErrors()){
            // TODO error handling
            return id;
        }

        return branchRepository.findById(id).map(branch -> {
            branch.changeInfo(branchDto.getName());
            return branch;
        }).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 병과입니다. id="+id)).getId();
    }

    @GetMapping("/{id}")
    public BranchDto get(@LoginUser SessionUser user, @PathVariable Long id){
        return branchRepository.findById(id)
                .map(BranchDto::new)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 병과입니다. id=" + id));
    }

    @GetMapping
    public List<BranchDto> getList(@LoginUser SessionUser user){
        return branchRepository.findAll().stream().map(BranchDto::new).collect(toList());
    }

    @DeleteMapping("/{id}")
    public Long delete(@LoginUser SessionUser user, @PathVariable Long id){
        branchRepository.deleteById(id);
        return id;
    }
}
