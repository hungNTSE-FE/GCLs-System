package com.gcl.crm.form;

import java.util.Date;
import javax.validation.constraints.*;
public class CustomerForm {
    private Long hdnCustomerCode;

    @NotBlank
    @Size(min = 3, max = 50)
    private String customerName;

    @NotNull
    private String gender;

    @NotBlank
    private String address;

    @Size(max = 10)
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String status;

    @NotBlank
    private String bankName;

    @NotBlank
    private String bankNumber;

    @NotBlank
    private String ownerBankingName;

    private Date dateOfBirth;

    @NotBlank
    @Size(max = 10)
    private String identifiNumber;

    @NotBlank
    private String dateOfIssue;

    @NotBlank
    private String placeOfIssue;

    @NotBlank
    private String accountCode;

    @NotBlank
    private String accountName;

    @NotBlank
    private String brokerCode;

    @NotBlank
    private String brokerName;

    private Date accountCreateDate;

    private Date registeredDate;

    @NotBlank
    private String contractStatus;

    @NotBlank
    private String hdnSourceId;

    private Date updDate;
    private String updUser;
    private ComboboxForm comboboxForm;

    public Long getHdnCustomerCode() {
        return hdnCustomerCode;
    }

    public void setHdnCustomerCode(Long hdnCustomerCode) {
        this.hdnCustomerCode = hdnCustomerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
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

    public Date getAccountCreateDate() {
        return accountCreateDate;
    }

    public void setAccountCreateDate(Date accountCreateDate) {
        this.accountCreateDate = accountCreateDate;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date updDate) {
        this.updDate = updDate;
    }

    public String getUpdUser() {
        return updUser;
    }

    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getIdentifiNumber() {
        return identifiNumber;
    }

    public void setIdentifiNumber(String identifiNumber) {
        this.identifiNumber = identifiNumber;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public ComboboxForm getComboboxForm() {
        return comboboxForm;
    }

    public void setComboboxForm(ComboboxForm comboboxForm) {
        this.comboboxForm = comboboxForm;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public String getOwnerBankingName() {
        return ownerBankingName;
    }

    public void setOwnerBankingName(String ownerBankingName) {
        this.ownerBankingName = ownerBankingName;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public String getHdnSourceId() {
        return hdnSourceId;
    }

    public void setHdnSourceId(String hdnSourceId) {
        this.hdnSourceId = hdnSourceId;
    }
}
