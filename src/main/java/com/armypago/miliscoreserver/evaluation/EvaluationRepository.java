package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    Optional<Evaluation> findByAuthorIdAndBranchId(Long authorId, Long branchId);

    List<Evaluation> findByBranchId(Long branchId);
}
