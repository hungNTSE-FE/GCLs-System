package com.gcl.crm.form;

import java.util.Collections;
import java.util.List;

public class CustomerStatusReportForm {
    List<CustomerStatusEvaluationForm> customerStatusEvaluationFormList;
    List<CustomerStatusForm> customerStatusFormList;
    String dateRange;
    Integer sumLevel1;
    Integer sumLevel2;
    Integer sumLevel3;
    Integer sumLevel4;
    Integer sumLevel5;
    Integer sumLevel6;
    Integer sumLevel7;
    Integer sumLevelTotal;
    Integer sumRegisteredAccount;
    Integer sumTopUp;
    Integer sumLot;

    public CustomerStatusReportForm() {
    }

    public CustomerStatusReportForm(List<CustomerStatusEvaluationForm> customerStatusEvaluationFormList, List<CustomerStatusForm> customerStatusFormList) {
        this.customerStatusEvaluationFormList = customerStatusEvaluationFormList;
        this.customerStatusFormList = customerStatusFormList;
    }

    public List<CustomerStatusEvaluationForm> getCustomerStatusEvaluationFormList() {
        return customerStatusEvaluationFormList;
    }

    public void setCustomerStatusEvaluationFormList(List<CustomerStatusEvaluationForm> customerStatusEvaluationFormList) {
        this.customerStatusEvaluationFormList = customerStatusEvaluationFormList;
    }

    public List<CustomerStatusForm> getCustomerStatusFormList() {
        return customerStatusFormList;
    }

    public void setCustomerStatusFormList(List<CustomerStatusForm> customerStatusFormList) {
        this.customerStatusFormList = customerStatusFormList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public Integer getSumLevel1() {
        return sumLevel1;
    }

    public void setSumLevel1(Integer sumLevel1) {
        this.sumLevel1 = sumLevel1;
    }

    public Integer getSumLevel2() {
        return sumLevel2;
    }

    public void setSumLevel2(Integer sumLevel2) {
        this.sumLevel2 = sumLevel2;
    }

    public Integer getSumLevel3() {
        return sumLevel3;
    }

    public void setSumLevel3(Integer sumLevel3) {
        this.sumLevel3 = sumLevel3;
    }

    public Integer getSumLevel4() {
        return sumLevel4;
    }

    public void setSumLevel4(Integer sumLevel4) {
        this.sumLevel4 = sumLevel4;
    }

    public Integer getSumLevel5() {
        return sumLevel5;
    }

    public void setSumLevel5(Integer sumLevel5) {
        this.sumLevel5 = sumLevel5;
    }

    public Integer getSumLevel6() {
        return sumLevel6;
    }

    public void setSumLevel6(Integer sumLevel6) {
        this.sumLevel6 = sumLevel6;
    }

    public Integer getSumLevel7() {
        return sumLevel7;
    }

    public void setSumLevel7(Integer sumLevel7) {
        this.sumLevel7 = sumLevel7;
    }

    public Integer getSumLevelTotal() {
        return sumLevelTotal;
    }

    public void setSumLevelTotal(Integer sumLevelTotal) {
        this.sumLevelTotal = sumLevelTotal;
    }

    public Integer getSumRegisteredAccount() {
        return sumRegisteredAccount;
    }

    public void setSumRegisteredAccount(Integer sumRegisteredAccount) {
        this.sumRegisteredAccount = sumRegisteredAccount;
    }

    public Integer getSumTopUp() {
        return sumTopUp;
    }

    public void setSumTopUp(Integer sumTopUp) {
        this.sumTopUp = sumTopUp;
    }

    public Integer getSumLot() {
        return sumLot;
    }

    public void setSumLot(Integer sumLot) {
        this.sumLot = sumLot;
    }
}
