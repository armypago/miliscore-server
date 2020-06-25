package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetail;
import com.armypago.miliscoreserver.branch.dto.BranchSimple;
import com.armypago.miliscoreserver.domain.branch.Branch;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.armypago.miliscoreserver.branch.BranchApi.*;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(BRANCH_URL)
@RestController
public class BranchApi {

    static final String BRANCH_URL = "/api/v1/branch";

    private final BranchRepository branchRepository;
    private final BranchQueryRepository branchQueryRepository;

    // TODO : 조건부 검색 추가
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody BranchDetail.Request request, Errors errors) {

        BranchDetail.Response response = null;
        if(!errors.hasErrors()){
            Branch branch = branchRepository.save(request.toEntity());
            response = new BranchDetail.Response(branch, null);
        }
        return !errors.hasErrors() ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errors.getAllErrors().get(0).getDefaultMessage());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @Valid @RequestBody BranchDetail.Request branchRequestDto, Errors errors){

        BranchDetail.Response response = null;
        if(!errors.hasErrors()){
            branchRepository.findById(id).ifPresent(branch -> {
                branch.changeInfo(branchRequestDto.getName());
            });
            response = branchQueryRepository.findById(id);
        }
        return !errors.hasErrors() ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 존재하는 병과명입니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK)
                .body(branchQueryRepository.findById(id));
    }

    @GetMapping
    public ResponseEntity<?> getList(){
        List<BranchSimple> response = branchRepository.findAll().stream().map(BranchSimple::new).collect(toList());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id){
        branchRepository.deleteById(id);
        return id;
    }
}
