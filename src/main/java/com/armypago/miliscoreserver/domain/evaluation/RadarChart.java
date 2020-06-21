package com.armypago.miliscoreserver.domain.evaluation;

import lombok.*;

import javax.persistence.Embeddable;

@Setter
@Getter
@NoArgsConstructor
@Embeddable
public class RadarChart {

    private double careerRelevance;     // 경력 연관성
    private double workLifeBalance;     // 워라밸
    private double unitVibe;            // 부대 분위기
    private double trainingIntensity;   // 훈련 강도
    private double officer;             // 간부
    private double dayOfLeaves;         // 휴가 일수

    @Builder
    public RadarChart(double careerRelevance, double workLifeBalance, double unitVibe,
                      double trainingIntensity, double officer, double dayOfLeaves){
        this.careerRelevance = careerRelevance;
        this.workLifeBalance = workLifeBalance;
        this.unitVibe = unitVibe;
        this.trainingIntensity = trainingIntensity;
        this.officer = officer;
        this.dayOfLeaves = dayOfLeaves;
    }
}
