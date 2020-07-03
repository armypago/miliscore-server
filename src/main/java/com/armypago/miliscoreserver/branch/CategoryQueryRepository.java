package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchSimple;
import com.armypago.miliscoreserver.branch.dto.CategoryDetail;
import com.armypago.miliscoreserver.domain.branch.Category;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.armypago.miliscoreserver.domain.branch.QBranch.branch;
import static com.armypago.miliscoreserver.domain.branch.QCategory.category;

@RequiredArgsConstructor
@Repository
public class CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CategoryDetail.Response findById(Long id){
        Category findCategory = queryFactory
                .selectFrom(category)
                .where(category.id.eq(id))
                .fetchOne();
        List<BranchSimple> branches = queryFactory
                .select(Projections.bean(BranchSimple.class,
                        branch.id, branch.name))
                .from(branch)
                .where(branch.category.id.eq(id)).fetch();
        return findCategory != null ? new CategoryDetail.Response(findCategory, branches) : null;
    }
}
