package com.gcl.crm.dto;

public class SourceEvaluationDto {
    private String sourceName;
    private Integer numOfPotential;
    private Double sourcePercent;
    private Integer sumOfPotential;

    public SourceEvaluationDto() {
    }

    public SourceEvaluationDto(String sourceName, Integer numOfPotential, Double sourcePercent, Integer sumOfPotential) {
        this.sourceName = sourceName;
        this.numOfPotential = numOfPotential;
        this.sourcePercent = sourcePercent;
        this.sumOfPotential = sumOfPotential;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getNumOfPotential() {
        return numOfPotential;
    }

    public void setNumOfPotential(Integer numOfPotential) {
        this.numOfPotential = numOfPotential;
    }

    public Double getSourcePercent() {
        return sourcePercent;
    }

    public void setSourcePercent(Double sourcePercent) {
        this.sourcePercent = sourcePercent;
    }

    public Integer getSumOfPotential() {
        return sumOfPotential;
    }

    public void setSumOfPotential(Integer sumOfPotential) {
        this.sumOfPotential = sumOfPotential;
    }
}
