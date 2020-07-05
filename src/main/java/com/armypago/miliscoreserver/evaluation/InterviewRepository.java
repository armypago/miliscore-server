package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.domain.evaluation.Interview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
