package com.armypago.miliscoreserver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MilitaryServiceStatus {

    NOTJOINED("NOTJOINED", "미필"),
    SERVING("SERVING", "복무중"),
    DISCHARGED("DISCHARGED", "군필");
    
    private final String key;
    private final String name;
}