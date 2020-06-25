package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.armypago.miliscoreserver.domain.branch.QBranch.branch;
import static com.armypago.miliscoreserver.domain.evaluation.QEvaluation.evaluation;
import static com.armypago.miliscoreserver.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class EvaluationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Evaluation findById(Long evaluationId){
        return queryFactory.selectFrom(evaluation)
                .leftJoin(evaluation.branch, branch).fetchJoin()
                .leftJoin(evaluation.author, user).fetchJoin()
                .where(evaluation.id.eq(evaluationId))
                .distinct()
                .fetchOne();
    }
}
