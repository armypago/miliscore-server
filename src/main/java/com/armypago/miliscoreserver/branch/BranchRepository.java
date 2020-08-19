package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.domain.branch.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    Optional<Branch> findByName(String name);
}
