package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetail;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationUpdate;
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
import java.util.Optional;

import static com.armypago.miliscoreserver.evaluation.EvaluationApi.*;

@Validated
@RequiredArgsConstructor
@RequestMapping(EVALUATION_URL)
@RestController
public class EvaluationApi {

    static final String EVALUATION_URL = "/api/v1/evaluation";

    private final EvaluationQueryRepository evaluationQueryRepository;
    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final EvaluationService evaluationService;

    // TODO Pageable
    
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody EvaluationDetail.Request request, Errors errors){
        EvaluationDetail.Response response = evaluationService.create(request);
        return response != null ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("병과 정보가 올바르지 않습니다.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable @InconsistentAuthor Long id,
                       @Valid @RequestBody EvaluationUpdate.Request request){

        Optional<EvaluationDetail.Response> response = evaluationRepository.findById(id).map(evaluation -> {
            evaluation.updateInfo(request.getContent(), request.getScore(), request.getDescription());
            return new EvaluationDetail.Response(evaluation, null);
        });
        return response.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body("평가 정보가 올바르지 않습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        EvaluationDetail.Response response = evaluationQueryRepository.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
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
