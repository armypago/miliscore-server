package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.domain.branch.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CateogryRepository extends JpaRepository<Category, Long> {
}
