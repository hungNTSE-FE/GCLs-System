package com.gcl.crm.enums;

import java.util.Arrays;

public enum PotentialRating {
    LOW(0)
    , MEDIUM(1)
    , HIGH(2);

    private Integer value;

    PotentialRating(Integer value){
        this.value = value;
    }

    public Integer getValue(){
        return this.value;
    }

    public static PotentialRating findByOption(Integer option){
        return Arrays
                .asList(values())
                .stream()
                .filter(rate -> rate.getValue().equals(option))
                .findFirst()
                .orElse(LOW);
    }
}
