package com.gcl.crm.form;

import com.gcl.crm.dto.SummaryCustomerManagement;
import com.gcl.crm.dto.SummaryMKTReport;

import java.util.List;
import java.util.Map;

public class MarketingSummaryReportForm {
    private String dateRange;
    private Map<String, SummaryMKTReport> summaryMKTGroupReport;
    private Map<String, SummaryMKTReport> summarySourceReport;
    private List<SummaryCustomerManagement> summaryCustomerManagementList;
    private Integer numTotalRegisteredMonth;
    private Integer numTotalTopUpMonth;
    private Integer numTotalLotMonth;
    public MarketingSummaryReportForm() {
    }

    public MarketingSummaryReportForm(String dateRange, Map<String, SummaryMKTReport> summaryMKTGroupReport, Map<String, SummaryMKTReport> summarySourceReport) {
        this.dateRange = dateRange;
        this.summaryMKTGroupReport = summaryMKTGroupReport;
        this.summarySourceReport = summarySourceReport;
    }

    public MarketingSummaryReportForm(String dateRange, Map<String, SummaryMKTReport> summaryMKTGroupReport, Map<String, SummaryMKTReport> summarySourceReport, List<SummaryCustomerManagement> summaryCustomerManagementList) {
        this.dateRange = dateRange;
        this.summaryMKTGroupReport = summaryMKTGroupReport;
        this.summarySourceReport = summarySourceReport;
        this.summaryCustomerManagementList = summaryCustomerManagementList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public Map<String, SummaryMKTReport> getSummaryMKTGroupReport() {
        return summaryMKTGroupReport;
    }

    public void setSummaryMKTGroupReport(Map<String, SummaryMKTReport> summaryMKTGroupReport) {
        this.summaryMKTGroupReport = summaryMKTGroupReport;
    }

    public Map<String, SummaryMKTReport> getSummarySourceReport() {
        return summarySourceReport;
    }

    public void setSummarySourceReport(Map<String, SummaryMKTReport> summarySourceReport) {
        this.summarySourceReport = summarySourceReport;
    }

    public List<SummaryCustomerManagement> getSummaryCustomerManagementList() {
        return summaryCustomerManagementList;
    }

    public void setSummaryCustomerManagementList(List<SummaryCustomerManagement> summaryCustomerManagementList) {
        this.summaryCustomerManagementList = summaryCustomerManagementList;
    }

    public Integer getNumTotalRegisteredMonth() {
        return numTotalRegisteredMonth;
    }

    public void setNumTotalRegisteredMonth(Integer numTotalRegisteredMonth) {
        this.numTotalRegisteredMonth = numTotalRegisteredMonth;
    }

    public Integer getNumTotalTopUpMonth() {
        return numTotalTopUpMonth;
    }

    public void setNumTotalTopUpMonth(Integer numTotalTopUpMonth) {
        this.numTotalTopUpMonth = numTotalTopUpMonth;
    }

    public Integer getNumTotalLotMonth() {
        return numTotalLotMonth;
    }

    public void setNumTotalLotMonth(Integer numTotalLotMonth) {
        this.numTotalLotMonth = numTotalLotMonth;
    }
}
