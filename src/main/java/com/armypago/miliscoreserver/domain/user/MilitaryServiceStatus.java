package com.armypago.miliscoreserver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MilitaryServiceStatus {

    NOTJOINED("NOTJOINED", "미필"),
    SERVING("SERVING", "복무중"),
    DISCHARGED("DISCHARGED", "군필");
    
    private final String key;
    private final String name;

    public static MilitaryServiceStatus findByKey(String key){
        return Arrays.stream(values())
                .filter(status -> status.getKey().equals(key))
                .findFirst().orElse(null);
    }
}