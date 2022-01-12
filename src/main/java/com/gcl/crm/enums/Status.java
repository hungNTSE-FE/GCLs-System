package com.gcl.crm.enums;

public enum Status {
    ACTIVE("1")
    , INACTIVE("0");

    private String value;

    Status(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
