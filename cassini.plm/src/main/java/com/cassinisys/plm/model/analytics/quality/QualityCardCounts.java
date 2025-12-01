package com.cassinisys.plm.model.analytics.quality;

import lombok.Data;

/**
 * Created by subramanyam on 19-07-2020.
 */
@Data
public class QualityCardCounts {
    private Integer inspectionPlans = 0;
    private Integer approvedInspectionPlans = 0;
    private Integer approvedInspections = 0;
    private Integer inspections = 0;
    private Integer problemReports = 0;
    private Integer implementedPrs = 0;
    private Integer ncrs = 0;
    private Integer implementedNcrs = 0;
    private Integer qcrs = 0;
    private Integer implementedQcrs = 0;
    private Integer ppap = 0;
    private Integer approvedPpap = 0;
    private Integer supplierAudits = 0;
    private Integer approvedSupplierAudits = 0;


}
