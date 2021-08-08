package com.gcl.crm.dto;

public class SummaryCustomerManagement {
    private String monthRange;
    private Integer numRegisteredAcc;
    private Integer numTopUpAcc;
    private Integer numLot;

    public SummaryCustomerManagement() {
    }

    public SummaryCustomerManagement(String monthRange, Integer numRegisteredAcc, Integer numTopUpAcc) {
        this.monthRange = monthRange;
        this.numRegisteredAcc = numRegisteredAcc;
        this.numTopUpAcc = numTopUpAcc;
    }

    public SummaryCustomerManagement(String monthRange, Integer numRegisteredAcc, Integer numTopUpAcc, Integer numLot) {
        this.monthRange = monthRange;
        this.numRegisteredAcc = numRegisteredAcc;
        this.numTopUpAcc = numTopUpAcc;
        this.numLot = numLot;
    }

    public String getMonthRange() {
        return monthRange;
    }

    public void setMonthRange(String monthRange) {
        this.monthRange = monthRange;
    }

    public Integer getNumRegisteredAcc() {
        return numRegisteredAcc;
    }

    public void setNumRegisteredAcc(Integer numRegisteredAcc) {
        this.numRegisteredAcc = numRegisteredAcc;
    }

    public Integer getNumTopUpAcc() {
        return numTopUpAcc;
    }

    public void setNumTopUpAcc(Integer numTopUpAcc) {
        this.numTopUpAcc = numTopUpAcc;
    }

    public Integer getNumLot() {
        return numLot;
    }

    public void setNumLot(Integer numLot) {
        this.numLot = numLot;
    }
}
