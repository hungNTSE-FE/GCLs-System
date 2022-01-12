package com.gcl.crm.dto;

import java.io.Serializable;
import java.util.Date;

public class CustomerDTO implements Serializable {

    private Long id;
    private String brokerCode;
    private String brokerName;
    private String brokerPhone;
    private String brokerEmail;
    private String tradingAccountCode;
    private String tradingAccountName;
    private String phoneNumber;
    private String email;
    private String address;
    private String identityNumber;
    private String birthDate;
    private String issueDate;
    private String issuePlace;
    private String bankName;
    private String bankCode;
    private String contractStatus;
    private String description;
    private String status;
    private String createDateContract;
    private String contractID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrokerCode() {
        return brokerCode;
    }

    public void setBrokerCode(String brokerCode) {
        this.brokerCode = brokerCode;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getBrokerPhone() {
        return brokerPhone;
    }

    public void setBrokerPhone(String brokerPhone) {
        this.brokerPhone = brokerPhone;
    }

    public String getBrokerEmail() {
        return brokerEmail;
    }

    public void setBrokerEmail(String brokerEmail) {
        this.brokerEmail = brokerEmail;
    }

    public String getTradingAccountCode() {
        return tradingAccountCode;
    }

    public void setTradingAccountCode(String tradingAccountCode) {
        this.tradingAccountCode = tradingAccountCode;
    }

    public String getTradingAccountName() {
        return tradingAccountName;
    }

    public void setTradingAccountName(String tradingAccountName) {
        this.tradingAccountName = tradingAccountName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getIssuePlace() {
        return issuePlace;
    }

    public void setIssuePlace(String issuePlace) {
        this.issuePlace = issuePlace;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getCreateDateContract() {
        return createDateContract;
    }

    public void setCreateDateContract(String createDateContract) {
        this.createDateContract = createDateContract;
    }

    public String getContractID() {
        return contractID;
    }

    public void setContractID(String contractID) {
        this.contractID = contractID;
    }
}
