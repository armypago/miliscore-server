package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.config.auth.LoginUser;
import com.armypago.miliscoreserver.config.auth.dto.SessionUser;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetailDto;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import sun.plugin.dom.exception.InvalidStateException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping(EvaluationApi.EVALUATION_URL)
@RestController
public class EvaluationApi {

    static final String EVALUATION_URL = "/api/v1/evaluation";

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    // TODO 예외 처리
    // TODO Pageable
    
    @PostMapping
    public Long create(@Valid @RequestBody EvaluationDetailDto.Request request, Errors errors){
        if(errors.hasErrors()){
            throw new IllegalArgumentException(errors
                    .getAllErrors().get(0).getDefaultMessage());
        }
        Optional<User> user = userRepository.findById(request.getAuthorId());
        Optional<Branch> branch = branchRepository.findById(request.getBranchId());
        if(!user.isPresent() || !branch.isPresent()){
            throw new IllegalArgumentException("병과 정보가 올바르지 않습니다.");
        }
        Evaluation evaluation = Evaluation.builder()
                .author(user.get()).branch(branch.get())
                .score(request.getScore()).content(request.getContent())
                .build();
        return evaluationRepository.save(evaluation).getId();
    }

    @PutMapping("/{id}")
    public Long update(@PathVariable Long id){
        return 0L;
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable Long id){

        return null;
    }

    @GetMapping
    public List<Object> getList(){

        return null;
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id){
        return 0L;
    }
}
