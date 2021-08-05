package com.gcl.crm.form;

public class CustomerStatusForm {
    private String marketing_name;
    private String level_0;
    private String level_1;
    private String level_2;
    private String level_3;
    private String level_4;
    private String level_5;
    private String level_6;
    private String level_7;
    private String total;

    public CustomerStatusForm() {
    }

    public CustomerStatusForm(String marketing_name, String level_0, String level_1, String level_2,
                              String level_3, String level_4, String level_5, String level_6,
                              String level_7, String total) {
        this.marketing_name = marketing_name;
        this.level_0 = level_0;
        this.level_1 = level_1;
        this.level_2 = level_2;
        this.level_3 = level_3;
        this.level_4 = level_4;
        this.level_5 = level_5;
        this.level_6 = level_6;
        this.level_7 = level_7;
        this.total = total;
    }

    public String getMarketing_name() {
        return marketing_name;
    }

    public void setMarketing_name(String marketing_name) {
        this.marketing_name = marketing_name;
    }

    public String getLevel_1() {
        return level_1;
    }

    public void setLevel_1(String level_1) {
        this.level_1 = level_1;
    }

    public String getLevel_2() {
        return level_2;
    }

    public void setLevel_2(String level_2) {
        this.level_2 = level_2;
    }

    public String getLevel_3() {
        return level_3;
    }

    public void setLevel_3(String level_3) {
        this.level_3 = level_3;
    }

    public String getLevel_4() {
        return level_4;
    }

    public void setLevel_4(String level_4) {
        this.level_4 = level_4;
    }

    public String getLevel_5() {
        return level_5;
    }

    public void setLevel_5(String level_5) {
        this.level_5 = level_5;
    }

    public String getLevel_6() {
        return level_6;
    }

    public void setLevel_6(String level_6) {
        this.level_6 = level_6;
    }

    public String getLevel_7() {
        return level_7;
    }

    public void setLevel_7(String level_7) {
        this.level_7 = level_7;
    }

    public String getLevel_0() {
        return level_0;
    }

    public void setLevel_0(String level_0) {
        this.level_0 = level_0;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
