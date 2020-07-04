package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetail;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.branch.QCategory;
import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.armypago.miliscoreserver.domain.branch.QBranch.*;
import static com.armypago.miliscoreserver.domain.branch.QCategory.category;
import static com.armypago.miliscoreserver.domain.evaluation.QEvaluation.*;

@RequiredArgsConstructor
@Repository
public class BranchQueryRepository {

    private final JPAQueryFactory queryFactory;

    public BranchDetail.Response findById(Long id){
        Branch findBranch  = queryFactory.selectFrom(branch)
                .leftJoin(branch.evaluations, evaluation).fetchJoin()
                .leftJoin(branch.category, category).fetchJoin()
                .where(branch.id.eq(id))
                .distinct()
                .fetchOne();

        RadarChart radarChart = queryFactory
                .select(Projections.bean(RadarChart.class,
                        evaluation.score.careerRelevance.avg().as("careerRelevance"),
                        evaluation.score.workLifeBalance.avg().as("workLifeBalance"),
                        evaluation.score.unitVibe.avg().as("unitVibe"),
                        evaluation.score.trainingIntensity.avg().as("trainingIntensity"),
                        evaluation.score.officer.avg().as("officer"),
                        evaluation.score.dayOfLeaves.avg().as("dayOfLeaves")))
                .from(evaluation)
                .where(evaluation.branch.id.eq(id))
                .fetchOne();

        return findBranch != null ? new BranchDetail.Response(findBranch, radarChart) : null;
    }
}
