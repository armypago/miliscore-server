package com.armypago.miliscoreserver.user;

import com.armypago.miliscoreserver.domain.user.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EducationRepository extends JpaRepository<Education, Long> {

    Optional<Education> findByPriority(int priority);
}
