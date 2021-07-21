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
    private Long potentialID;

    public PotentialSearchForm() {
    }

    public PotentialSearchForm(String name, String phone, String email, String source, String time, Integer level, Long potentialID) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.source = source;
        this.time = time;
        this.level = level;
        this.potentialID = potentialID;
    }

    public PotentialSearchForm(Long potentialID, String name, String phone, String email, String source, String time) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.source = source;
        this.time = time;
        this.potentialID = potentialID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Long getPotentialID() {
        return potentialID;
    }

    public void setPotentialID(Long potentialID) {
        this.potentialID = potentialID;
    }
}
