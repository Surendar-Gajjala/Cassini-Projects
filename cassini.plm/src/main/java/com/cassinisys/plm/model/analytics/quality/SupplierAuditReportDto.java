package com.cassinisys.plm.model.analytics.quality;

import java.util.LinkedList;
import java.util.List;

import lombok.Data;

@Data
public class SupplierAuditReportDto {


    private List<String> years = new LinkedList<>();

    private List<MonthInspectionReportDto> yearReports = new LinkedList<>();
    
}
