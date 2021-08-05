package com.gcl.crm.form;

import com.gcl.crm.dto.SourceEvaluationDto;

import java.util.List;

public class SourceEvaluationForm {
    private List<SourceEvaluationDto> sourceEvaluationDtoList;
    private String dateRange;
    private Integer total;

    public SourceEvaluationForm() {
    }

    public List<SourceEvaluationDto> getSourceEvaluationDtoList() {
        return sourceEvaluationDtoList;
    }

    public void setSourceEvaluationDtoList(List<SourceEvaluationDto> sourceEvaluationDtoList) {
        this.sourceEvaluationDtoList = sourceEvaluationDtoList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
