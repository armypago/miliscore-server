package com.armypago.miliscoreserver.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Getter
@RequiredArgsConstructor
public enum MilitaryServiceStatus {

    NOTJOINED("NOTJOINED", "미필"),
    SERVING("SERVING", "복무중"),
    DISCHARGED("DISCHARGED", "군필");
    
    private final String key;
    private final String name;

    public static MilitaryServiceStatus findByName(String name){
        return Arrays.stream(values())
                .filter(status -> status.getName().endsWith(name))
                .findFirst().orElse(null);
    }

    public static MilitaryServiceStatus findByKey(String key){
        return Arrays.stream(values())
                .filter(status -> status.getKey().equals(key))
                .findFirst().orElse(null);
    }

    public static List<String> getList(){
        return Stream.of(MilitaryServiceStatus.values())
                .map(MilitaryServiceStatus::getName).collect(toList());
    }
}