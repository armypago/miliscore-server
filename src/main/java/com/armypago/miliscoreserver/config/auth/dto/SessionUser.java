package com.armypago.miliscoreserver.config.auth.dto;

import com.armypago.miliscoreserver.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

// TODO : SessionUser 필요 정보 수정
// TODO : 실명제?

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
