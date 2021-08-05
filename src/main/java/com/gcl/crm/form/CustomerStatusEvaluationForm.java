package com.gcl.crm.form;

public class CustomerStatusEvaluationForm {
    private String marketing_name;
    private Integer num_of_registered_account;
    private Integer num_of_top_up_account;
    private Integer num_of_lot;

    public CustomerStatusEvaluationForm() {
    }

    public CustomerStatusEvaluationForm(String marketing_name, Integer num_of_registered_account, Integer num_of_top_up_account) {
        this.marketing_name = marketing_name;
        this.num_of_registered_account = num_of_registered_account;
        this.num_of_top_up_account = num_of_top_up_account;
    }

    public CustomerStatusEvaluationForm(String marketing_name, Integer num_of_registered_account, Integer num_of_top_up_account, Integer num_of_lot) {
        this.marketing_name = marketing_name;
        this.num_of_registered_account = num_of_registered_account;
        this.num_of_top_up_account = num_of_top_up_account;
        this.num_of_lot = num_of_lot;
    }

    public String getMarketing_name() {
        return marketing_name;
    }

    public void setMarketing_name(String marketing_name) {
        this.marketing_name = marketing_name;
    }

    public Integer getNum_of_registered_account() {
        return num_of_registered_account;
    }

    public void setNum_of_registered_account(Integer num_of_registered_account) {
        this.num_of_registered_account = num_of_registered_account;
    }

    public Integer getNum_of_top_up_account() {
        return num_of_top_up_account;
    }

    public void setNum_of_top_up_account(Integer num_of_top_up_account) {
        this.num_of_top_up_account = num_of_top_up_account;
    }

    public Integer getNum_of_lot() {
        return num_of_lot;
    }

    public void setNum_of_lot(Integer num_of_lot) {
        this.num_of_lot = num_of_lot;
    }
}
