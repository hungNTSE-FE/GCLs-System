package com.gcl.crm.form;

public class EmployeeSearchForm {
    private Long id;
    private String name;

    public EmployeeSearchForm() {
    }

    public EmployeeSearchForm(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
