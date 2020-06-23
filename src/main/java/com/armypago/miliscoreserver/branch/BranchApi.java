package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetailDto;
import com.armypago.miliscoreserver.branch.dto.BranchListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
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
    private final BranchQueryRepository branchQueryRepository;

    // TODO : APP 용 오류 메시지, ResponseBody or ResponseEntity 형식으로 변경
    // TODO : 조건부 검색 추가
    
    @PostMapping
    public Long create(@Valid @RequestBody BranchDetailDto.Request request,
                       BindingResult bindingResult) throws IllegalArgumentException {

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult
                    .getAllErrors().get(0).getDefaultMessage());
        }
        return branchRepository.save(request.toEntity()).getId();
    }

    @PutMapping("/{id}")
    public Long update(@PathVariable Long id, BindingResult bindingResult,
                       @Valid @RequestBody BranchDetailDto.Request branchRequestDto){

        if(bindingResult.hasErrors()){
            throw new IllegalArgumentException(bindingResult
                    .getAllErrors().get(0).getDefaultMessage());
        }
        return branchRepository.findById(id).map(branch -> {
            branch.changeInfo(branchRequestDto.getName());
            return branch.getId();
        }).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 병과입니다. id="+id));
    }

    @GetMapping("/{id}")
    public BranchDetailDto.Response get(@PathVariable Long id){
        return branchQueryRepository.findById(id);
    }

    @GetMapping
    public List<BranchListDto> getList(){
        return branchRepository.findAll().stream().map(BranchListDto::new).collect(toList());
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id){
        branchRepository.deleteById(id);
        return id;
    }
}
