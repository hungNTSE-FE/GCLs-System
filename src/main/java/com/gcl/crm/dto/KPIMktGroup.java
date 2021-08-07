package com.gcl.crm.dto;

public class KPIMktGroup {

    private Long mktID;

    private String mktName;

    private Integer numPot;

    private Integer numLot;

    private Double KPI;

    private Integer numRegisteredAcc;

    private Integer numTopUp;

    public KPIMktGroup() {
    }

    public KPIMktGroup(Long mktID, String mktName, Integer numPot, Integer numLot, Double KPI, Integer numRegisteredAcc, Integer numTopUp) {
        this.mktID = mktID;
        this.mktName = mktName;
        this.numPot = numPot;
        this.numLot = numLot;
        this.KPI = KPI;
        this.numRegisteredAcc = numRegisteredAcc;
        this.numTopUp = numTopUp;
    }

    public Long getMktID() {
        return mktID;
    }

    public void setMktID(Long mktID) {
        this.mktID = mktID;
    }

    public String getMktName() {
        return mktName;
    }

    public void setMktName(String mktName) {
        this.mktName = mktName;
    }

    public Integer getNumPot() {
        return numPot;
    }

    public void setNumPot(Integer numPot) {
        this.numPot = numPot;
    }

    public Integer getNumLot() {
        return numLot;
    }

    public void setNumLot(Integer numLot) {
        this.numLot = numLot;
    }

    public Double getKPI() {
        return KPI;
    }

    public void setKPI(Double KPI) {
        this.KPI = KPI;
    }

    public Integer getNumRegisteredAcc() {
        return numRegisteredAcc;
    }

    public void setNumRegisteredAcc(Integer numRegisteredAcc) {
        this.numRegisteredAcc = numRegisteredAcc;
    }

    public Integer getNumTopUp() {
        return numTopUp;
    }

    public void setNumTopUp(Integer numTopUp) {
        this.numTopUp = numTopUp;
    }
}
