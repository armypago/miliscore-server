package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.config.auth.LoginUser;
import com.armypago.miliscoreserver.config.auth.dto.SessionUser;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetailDto;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping(EvaluationApi.EVALUATION_URL)
@RestController
public class EvaluationApi {

    static final String EVALUATION_URL = "/api/v1/evaluation";

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    // TODO 로그인 상태 확인
    // TODO 예외 처리
    // TODO Pageable
    
    @PostMapping
    public Long create(@LoginUser SessionUser user,
                       @RequestBody EvaluationDetailDto.Request request){
        // TODO valid로 빼기
        User findUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new InvalidStateException("사용자 정보가 올바르지 않습니다."));
        boolean present = evaluationRepository
                .findByAuthorIdAndBranchId(findUser.getId(), request.getBranchId())
                .isPresent();
        if(present){
            throw new IllegalArgumentException("이미 평가를 작성했습니다.");
        }
        return branchRepository.findById(request.getBranchId()).map(branch -> {
            Evaluation evaluation = Evaluation.builder()
                    .author(findUser).branch(branch)
                    .score(request.getScore()).content(request.getContent())
                    .build();
            return evaluationRepository.save(evaluation).getId();
        }).orElseThrow(() -> new IllegalArgumentException("병과 정보가 올바르지 않습니다."));
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
