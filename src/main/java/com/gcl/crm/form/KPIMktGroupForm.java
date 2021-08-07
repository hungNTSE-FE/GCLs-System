package com.gcl.crm.form;

import com.gcl.crm.dto.KPIMktGroup;

import java.util.List;

public class KPIMktGroupForm {
    List<KPIMktGroup> kpiMktGroupList;
    String dateRange;

    public KPIMktGroupForm() {
    }

    public KPIMktGroupForm(List<KPIMktGroup> kpiMktGroupList, String dateRange) {
        this.kpiMktGroupList = kpiMktGroupList;
        this.dateRange = dateRange;
    }

    public List<KPIMktGroup> getKpiMktGroupList() {
        return kpiMktGroupList;
    }

    public void setKpiMktGroupList(List<KPIMktGroup> kpiMktGroupList) {
        this.kpiMktGroupList = kpiMktGroupList;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }
}
