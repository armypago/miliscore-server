package com.armypago.miliscoreserver.user.dto;

import com.armypago.miliscoreserver.domain.evaluation.RadarChart;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

public class UserUpdate {

    @Setter
    @NoArgsConstructor
    @Getter
    public static class Request {

        @NotNull
        private String content;
        private RadarChart score;

        public Request(String content, RadarChart score){
            this.content = content;
            this.score = score;
        }
    }
}
