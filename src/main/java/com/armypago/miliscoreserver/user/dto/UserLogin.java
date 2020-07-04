package com.armypago.miliscoreserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserLogin {

    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Session {

        private Long id;
        private String name;
    }
}
