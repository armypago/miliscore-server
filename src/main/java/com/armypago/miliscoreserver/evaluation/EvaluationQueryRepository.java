package com.armypago.miliscoreserver.evaluation;

import com.armypago.miliscoreserver.domain.evaluation.Evaluation;
import com.armypago.miliscoreserver.domain.evaluation.Interview;
import com.armypago.miliscoreserver.domain.evaluation.QEvaluation;
import com.armypago.miliscoreserver.domain.evaluation.QInterview;
import com.armypago.miliscoreserver.evaluation.dto.EvaluationDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.armypago.miliscoreserver.domain.branch.QBranch.branch;
import static com.armypago.miliscoreserver.domain.evaluation.QEvaluation.evaluation;
import static com.armypago.miliscoreserver.domain.evaluation.QInterview.interview;
import static com.armypago.miliscoreserver.domain.user.QUser.user;

@RequiredArgsConstructor
@Repository
public class EvaluationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public EvaluationDetail.Response findById(Long evaluationId){
        Evaluation eval = queryFactory.selectFrom(evaluation)
                .leftJoin(evaluation.branch, branch).fetchJoin()
                .leftJoin(evaluation.author, user).fetchJoin()
                .where(evaluation.id.eq(evaluationId))
                .distinct()
                .fetchOne();
        List<Interview> interviews = queryFactory
                .selectFrom(interview)
                .where(interview.evaluation.id.eq(evaluationId))
                .fetch();
        assert eval != null;
        return new EvaluationDetail.Response(eval, interviews);
    }
}
