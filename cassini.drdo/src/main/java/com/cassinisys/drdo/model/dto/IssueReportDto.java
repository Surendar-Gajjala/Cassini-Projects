package com.cassinisys.drdo.model.dto;

import com.cassinisys.drdo.model.bom.BomInstanceItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by subramanyam reddy on 16-09-2019.
 */
public class IssueReportDto {

    BomInstanceItem section;

    List<SubsystemIssueReport> subsystemReport = new ArrayList<>();

    public BomInstanceItem getSection() {
        return section;
    }

    public void setSection(BomInstanceItem section) {
        this.section = section;
    }

    public List<SubsystemIssueReport> getSubsystemReport() {
        return subsystemReport;
    }

    public void setSubsystemReport(List<SubsystemIssueReport> subsystemReport) {
        this.subsystemReport = subsystemReport;
    }
}
