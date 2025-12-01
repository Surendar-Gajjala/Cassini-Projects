package com.cassinisys.plm.controller.analytics.quality;

import com.cassinisys.platform.api.core.BaseController;
import com.cassinisys.platform.util.PageRequestConverter;
import com.cassinisys.plm.model.analytics.quality.*;
import com.cassinisys.plm.service.analytics.quality.QualityAnalyticsService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by subramanyam on 17-07-2020.
 */
@RestController
@RequestMapping("/dashboards/qualities")
@Api(tags = "PLM.ANALYTICS",description = "Analytics Related")
public class QualityAnalyticsController extends BaseController {

    @Autowired
    private QualityAnalyticsService qualityAnalyticsService;
    @Autowired
    private PageRequestConverter pageRequestConverter;

    @RequestMapping(value = "/types", method = RequestMethod.GET)
    public QualityByTypeDto getQualitiesByType() {
        return qualityAnalyticsService.getQualitiesByType();
    }

    @RequestMapping(value = "/counts", method = RequestMethod.GET)
    public QualityByTypeDto getStaticDashboardCounts() {
        return qualityAnalyticsService.getStaticDashboardCounts();
    }

    @RequestMapping(value = "/card/counts", method = RequestMethod.GET)
    public QualityCardCounts getQualityDashboardCounts() {
        return qualityAnalyticsService.getQualityDashboardCounts();
    }

    @RequestMapping(value = "/inspectionreports/counts", method = RequestMethod.GET)
    public InspectionReportDto getMfrInspectionReportCounts() {
        return qualityAnalyticsService.getMfrInspectionReportCounts();
    }

    @RequestMapping(value = "/ppapchecklist/status/counts", method = RequestMethod.GET)
    public PPAPChecklistStatusDto getPPAPChecklistStatus() {
        return qualityAnalyticsService.getPPAPChecklistStatus();
    }

    @RequestMapping(value = "/inspectionplans/status", method = RequestMethod.GET)
    public QualityByTypeDto getInspectionPlansByStatus() {
        return qualityAnalyticsService.getInspectionPlansByStatus();
    }

    @RequestMapping(value = "/inspections/status", method = RequestMethod.GET)
    public QualityByTypeDto getInspectionsByStatus() {
        return qualityAnalyticsService.getInspectionsByStatus();
    }

    @RequestMapping(value = "/problemreports/status", method = RequestMethod.GET)
    public QualityByTypeDto getProblemReportsByStatus() {
        return qualityAnalyticsService.getProblemReportsByStatus();
    }

    @RequestMapping(value = "/problemreports/source", method = RequestMethod.GET)
    public QualityByTypeDto getProblemReportsBySource() {
        return qualityAnalyticsService.getProblemReportsBySource();
    }

    @RequestMapping(value = "/problemreports/severity", method = RequestMethod.GET)
    public ProblemReportCounts getProblemReportsBySeverity() {
        return qualityAnalyticsService.getProblemReportsBySeverity();
    }

    @RequestMapping(value = "/problemreports/failure", method = RequestMethod.GET)
    public ProblemReportCounts getProblemReportsByFailure() {
        return qualityAnalyticsService.getProblemReportsByFailure();
    }

    @RequestMapping(value = "/problemreports/disposition", method = RequestMethod.GET)
    public ProblemReportCounts getProblemReportsByDisposition() {
        return qualityAnalyticsService.getProblemReportsByDisposition();
    }

    @RequestMapping(value = "/ncrs/severity", method = RequestMethod.GET)
    public ProblemReportCounts getNcrsBySeverity() {
        return qualityAnalyticsService.getNcrsBySeverity();
    }

    @RequestMapping(value = "/ncrs/failure", method = RequestMethod.GET)
    public ProblemReportCounts getNcrsByFailure() {
        return qualityAnalyticsService.getNcrsByFailure();
    }

    @RequestMapping(value = "/ncrs/disposition", method = RequestMethod.GET)
    public ProblemReportCounts getNcrsByDisposition() {
        return qualityAnalyticsService.getNcrsByDisposition();
    }

    @RequestMapping(value = "/ncrs/status", method = RequestMethod.GET)
    public QualityByTypeDto getNcrsByStatus() {
        return qualityAnalyticsService.getNcrsByStatus();
    }

    @RequestMapping(value = "/qcrs/status", method = RequestMethod.GET)
    public QualityByTypeDto getQcrsByStatus() {
        return qualityAnalyticsService.getQcrsByStatus();
    }

    @RequestMapping(value = "/qcrs/type", method = RequestMethod.GET)
    public QualityByTypeDto getQcrsByType() {
        return qualityAnalyticsService.getQcrsByType();
    }

    @RequestMapping(value = "/severities/failures/dispositions", method = RequestMethod.GET)
    public ProblemReportCounts getObjectSeverityFailureDispositions() {
        return qualityAnalyticsService.getObjectSeverityFailureDispositions();
    }

    @RequestMapping(value = "/top/inspection/failure/products", method = RequestMethod.GET)
    private List<InspectionFailureProducts> getTopInspectionFailureProducts() {
        return qualityAnalyticsService.getTopInspectionFailureProducts();
    }

    @RequestMapping(value = "/top/inspection/failure/materials", method = RequestMethod.GET)
    private List<InspectionFailureMaterials> getTopInspectionFailureMaterials() {
        return qualityAnalyticsService.getTopInspectionFailureMaterials();
    }

    @RequestMapping(value = "/top/customer/reporting/problems", method = RequestMethod.GET)
    private List<CustomerReportingProblems> getTopCustomerReportingProblems() {
        return qualityAnalyticsService.getTopCustomerReportingProblems();
    }

    @RequestMapping(value = "/top/product/problems", method = RequestMethod.GET)
    private List<ProductProblems> getTopProductProblems() {
        return qualityAnalyticsService.getTopProductProblems();
    }

    @RequestMapping(value = "/top/manufacurersforncr", method = RequestMethod.GET)
    private List<ManufacturersForNCR> getTopManufacturersForNCR() {
        return qualityAnalyticsService.getTopManufacturersForNCR();
    }
    
    @RequestMapping(value = "/supplierauditreport/counts", method = RequestMethod.GET)
    public SupplierAuditReportDto getSupplierAuditReportCounts() {
        return qualityAnalyticsService.getSupplierAuditReportCounts();
    }

}
