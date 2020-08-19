package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.Interview;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetail;
import com.armypago.miliscoreserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    public EvaluationDetail.Response create(EvaluationDetail.Request request){
        Optional<User> user = userRepository.findById(request.getAuthorId());
        Optional<Branch> branch = branchRepository.findById(request.getBranchId());

        if(!user.isPresent() || !branch.isPresent()){
            return null;
        }
        Evaluation evaluation = evaluationRepository.save(Evaluation.builder()
                .author(user.get()).branch(branch.get())
                .score(request.getScore()).content(request.getContent())
                .description(request.getDescription())
                .build());
        List<Interview> interviews = interviewRepository
                .saveAll(request.getInterviews(evaluation));
        return new EvaluationDetail.Response(evaluation, interviews);
    }
}
