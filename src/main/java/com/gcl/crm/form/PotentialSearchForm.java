package com.gcl.crm.form;

import lombok.Data;

@Data
public class PotentialSearchForm {
    private String name;
    private String phone;
    private String email;
    private String source;
    private String time;
    private Integer level;
}
