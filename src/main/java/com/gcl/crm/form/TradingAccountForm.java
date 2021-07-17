package com.gcl.crm.form;

public class TradingAccountForm {
    private String phoneNumber;
    private String email;
    private String accountNumber;
    private String accountName;
    private String brokerCode;
    private String brokeName;

    public TradingAccountForm() {
    }

    public TradingAccountForm(String phoneNumber, String email, String accountNumber, String accountName, String brokerCode, String brokeName) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.accountNumber = accountNumber;
        this.accountName = accountName;
        this.brokerCode = brokerCode;
        this.brokeName = brokeName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getBrokeName() {
        return brokeName;
    }

    public void setBrokeName(String brokeName) {
        this.brokeName = brokeName;
    }
}
