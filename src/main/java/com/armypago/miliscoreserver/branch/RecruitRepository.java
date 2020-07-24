package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.domain.branch.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {

    List<Recruit> findAllByBranchId(Long id);
}
