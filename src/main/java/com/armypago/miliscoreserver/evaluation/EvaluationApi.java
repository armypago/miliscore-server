package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetailDto;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationUpdateDto;
import com.armypago.miliscoreserver.evaluation.validator.InconsistentAuthor;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RequiredArgsConstructor
@RequestMapping(EvaluationApi.EVALUATION_URL)
@RestController
public class EvaluationApi {

    static final String EVALUATION_URL = "/api/v1/evaluation";

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    // TODO Pageable
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EvaluationDetailDto.Request request){

        EvaluationDetailDto.Response response = null;
        Optional<User> user = userRepository.findById(request.getAuthorId());
        Optional<Branch> branch = branchRepository.findById(request.getBranchId());

        if(user.isPresent() && branch.isPresent()){
            Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                    .author(user.get()).branch(branch.get())
                    .score(request.getScore()).content(request.getContent())
                    .build());
            response = new EvaluationDetailDto.Response(evaluation);
        }
        return response != null ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("병과 정보가 올바르지 않습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable @InconsistentAuthor Long id,
                       @Valid @RequestBody EvaluationUpdateDto.Request request){

        Optional<EvaluationDetailDto.Response> response = evaluationRepository.findById(id).map(evaluation -> {
            evaluation.updateInfo(request.getContent(), request.getScore());
            return new EvaluationDetailDto.Response(evaluation);
        });
        return response.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("평가 정보가 올바르지 않습니다.");
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Object exception(Exception e) {
        String[] message = e.getMessage().split(": ");
        return message[message.length-1];
    }
}
