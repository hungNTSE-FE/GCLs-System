package com.gcl.crm.entity;

import java.io.Serializable;

public class CustomerId implements Serializable {

    private String customerCode;

    public CustomerId() {
    }

    public CustomerId(String customer_code) {
        this.customerCode = customer_code;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
