define(
    [
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/shared/factories/httpFactory'
    ],


    function (mdoule) {
        mdoule.factory('AnalyticsService', AnalyticsService);

        function AnalyticsService(httpFactory) {
            return {
                getQualityTypeCounts: getQualityTypeCounts,
                getQualityDashboardCounts: getQualityDashboardCounts,
                getQualityCardCounts: getQualityCardCounts,
                getInspectionPlansByStatus: getInspectionPlansByStatus,
                getInspectionsByStatus: getInspectionsByStatus,
                getPrsByStatus: getPrsByStatus,
                getPrsBySource: getPrsBySource,
                getPrsBySeverities: getPrsBySeverities,
                getPrsByFailures: getPrsByFailures,
                getPrsByDispositions: getPrsByDispositions,
                getNcrsByStatus: getNcrsByStatus,
                getNcrsBySeverities: getNcrsBySeverities,
                getNcrsByFailures: getNcrsByFailures,
                getNcrsByDispositions: getNcrsByDispositions,
                getQcrsByStatus: getQcrsByStatus,
                getQcrsByType: getQcrsByType,
                getObjectSeverityFailureDispositions: getObjectSeverityFailureDispositions,
                getChangeDashboardCounts: getChangeDashboardCounts,
                getChangeCardCounts: getChangeCardCounts,
                getChangeTypeCounts: getChangeTypeCounts,
                getItemDashboardCounts: getItemDashboardCounts,
                getItemClassCounts: getItemClassCounts,
                getProductItemByStatusCounts: getProductItemByStatusCounts,
                getItemConfigurationCounts: getItemConfigurationCounts,
                getOEMDashboardCounts: getOEMDashboardCounts,
                getProjectStatusCounts: getProjectDashboardCounts,
                getItemClassCardCounts: getItemClassCardCounts,
                getItemsByLifeCyclePhases: getItemsByLifeCyclePhases,
                getWorkflowDashboardCounts: getWorkflowDashboardCounts,
                getChangeWorkflowCounts: getChangeWorkflowCounts,
                getAllStartedWorkflows: getAllStartedWorkflows,
                getWorkflowTypeCardCounts: getWorkflowTypeCardCounts,
                getTopInspectionFailureProducts: getTopInspectionFailureProducts,
                getTopProductProblems: getTopProductProblems,
                getTopCustomerReportingProblems: getTopCustomerReportingProblems,
                getTopManufacturersForNCR: getTopManufacturersForNCR,
                getTopMfrParts: getTopMfrParts,
                getTopProblemMfrParts: getTopProblemMfrParts,
                getTopProblemMfrs: getTopProblemMfrs,
                getTopRecurringParts: getTopRecurringParts,
                getTopProblemItems: getTopProblemItems,
                getTopProblemItemTypes: getTopProblemItemTypes,
                getTopChangingItemTypes: getTopChangingItemTypes,
                getTopProductMaterials: getTopProductMaterials,
                getTopInspectionFailureMaterials: getTopInspectionFailureMaterials,
                getMfrInspectionReportCounts: getMfrInspectionReportCounts,
                getPPAPChecklistStatus: getPPAPChecklistStatus,
                getPPAPCheckListStatusCounts: getPPAPCheckListStatusCounts,
                getProjectStatusByProgramDashboardCounts: getProjectStatusByProgramDashboardCounts,
                getSupplierAuditReportCounts: getSupplierAuditReportCounts,
                getProgramDrillDownReport: getProgramDrillDownReport
            };

            function getQualityTypeCounts() {
                var url = "api/dashboards/qualities/types";
                return httpFactory.get(url);
            }

            function getQualityDashboardCounts() {
                var url = "api/dashboards/qualities/counts";
                return httpFactory.get(url);
            }

            function getQualityCardCounts() {
                var url = "api/dashboards/qualities/card/counts";
                return httpFactory.get(url);
            }

            function getMfrInspectionReportCounts() {
                var url = "api/dashboards/qualities/inspectionreports/counts";
                return httpFactory.get(url);
            }

            function getPPAPChecklistStatus() {
                var url = "api/dashboards/qualities/ppapchecklist/status";
                return httpFactory.get(url);
            }

            function getInspectionPlansByStatus() {
                var url = "api/dashboards/qualities/inspectionplans/status";
                return httpFactory.get(url);
            }

            function getInspectionsByStatus() {
                var url = "api/dashboards/qualities/inspections/status";
                return httpFactory.get(url);
            }

            function getPrsByStatus() {
                var url = "api/dashboards/qualities/problemreports/status";
                return httpFactory.get(url);
            }

            function getPrsBySource() {
                var url = "api/dashboards/qualities/problemreports/source";
                return httpFactory.get(url);
            }

            function getPrsBySeverities() {
                var url = "api/dashboards/qualities/problemreports/severity";
                return httpFactory.get(url);
            }

            function getPrsByFailures() {
                var url = "api/dashboards/qualities/problemreports/failure";
                return httpFactory.get(url);
            }

            function getPrsByDispositions() {
                var url = "api/dashboards/qualities/problemreports/disposition";
                return httpFactory.get(url);
            }

            function getNcrsByStatus() {
                var url = "api/dashboards/qualities/ncrs/status";
                return httpFactory.get(url);
            }

            function getNcrsBySeverities() {
                var url = "api/dashboards/qualities/ncrs/severity";
                return httpFactory.get(url);
            }

            function getNcrsByFailures() {
                var url = "api/dashboards/qualities/ncrs/failure";
                return httpFactory.get(url);
            }

            function getNcrsByDispositions() {
                var url = "api/dashboards/qualities/ncrs/disposition";
                return httpFactory.get(url);
            }

            function getQcrsByStatus() {
                var url = "api/dashboards/qualities/qcrs/status";
                return httpFactory.get(url);
            }

            function getQcrsByType() {
                var url = "api/dashboards/qualities/qcrs/type";
                return httpFactory.get(url);
            }

            function getObjectSeverityFailureDispositions() {
                var url = "api/dashboards/qualities/severities/failures/dispositions";
                return httpFactory.get(url);
            }

            function getChangeDashboardCounts() {
                var url = "api/dashboards/changes/counts";
                return httpFactory.get(url);
            }

            function getChangeCardCounts() {
                var url = "api/dashboards/changes/card/counts";
                return httpFactory.get(url);
            }

            function getChangeTypeCounts() {
                var url = "api/dashboards/changes/type/counts";
                return httpFactory.get(url);
            }

            function getItemDashboardCounts() {
                var url = "api/dashboards/items/dashboard/counts";
                return httpFactory.get(url);
            }

            function getItemConfigurationCounts() {
                var url = "api/dashboards/items/configuration/counts";
                return httpFactory.get(url);
            }

            function getItemClassCounts() {
                var url = "api/dashboards/items/class/counts";
                return httpFactory.get(url);
            }

            function getItemClassCardCounts() {
                var url = "api/dashboards/items/class/card/counts";
                return httpFactory.get(url);
            }

            function getItemsByLifeCyclePhases() {
                var url = "api/dashboards/items/lifecycle/counts";
                return httpFactory.get(url);
            }

            function getProductItemByStatusCounts() {
                var url = "api/dashboards/items/products/status/counts";
                return httpFactory.get(url);
            }

            function getOEMDashboardCounts() {
                var url = "api/dashboards/oems/counts";
                return httpFactory.get(url);
            }

            function getWorkflowDashboardCounts() {
                var url = "api/dashboards/workflow/counts";
                return httpFactory.get(url);
            }

            function getChangeWorkflowCounts() {
                var url = "api/dashboards/workflow/objectType/change";
                return httpFactory.get(url);
            }

            function getAllStartedWorkflows() {
                var url = "api/dashboards/workflow/started/all";
                return httpFactory.get(url);
            }

            function getProjectDashboardCounts() {
                var url = "api/dashboards/projects/counts";
                return httpFactory.get(url);
            }
            function getProgramDrillDownReport() {
                var url = "api/dashboards/projects/program/drilldown/report";
                return httpFactory.get(url);
            }

            function getProjectStatusByProgramDashboardCounts() {
                var url = "api/dashboards/projects/program/counts";
                return httpFactory.get(url);
            }

            function getWorkflowTypeCardCounts() {
                var url = "api/dashboards/workflow/type/card/counts";
                return httpFactory.get(url);
            }

            function getTopInspectionFailureProducts() {
                var url = "api/dashboards/qualities/top/inspection/failure/products";
                return httpFactory.get(url);
            }

            function getTopInspectionFailureMaterials() {
                var url = "api/dashboards/qualities/top/inspection/failure/materials";
                return httpFactory.get(url);
            }

            function getTopProductProblems() {
                var url = "api/dashboards/qualities/top/product/problems";
                return httpFactory.get(url);
            }

            function getTopProductMaterials() {
                var url = "api/dashboards/qualities/top/product/materials";
                return httpFactory.get(url);
            }

            function getTopCustomerReportingProblems() {
                var url = "api/dashboards/qualities/top/customer/reporting/problems";
                return httpFactory.get(url);
            }

            function getTopManufacturersForNCR() {
                var url = "api/dashboards/qualities/top/manufacurersforncr";
                return httpFactory.get(url);
            }

            function getTopMfrParts() {
                var url = "api/dashboards/oems/mfrs/parts";
                return httpFactory.get(url);
            }

            function getTopProblemMfrParts() {
                var url = "api/dashboards/oems/problem/parts";
                return httpFactory.get(url);
            }

            function getTopProblemMfrs() {
                var url = "api/dashboards/oems/problem/mfrs";
                return httpFactory.get(url);
            }

            function getTopRecurringParts() {
                var url = "api/dashboards/oems/recurring/parts";
                return httpFactory.get(url);
            }

            function getTopProblemItems() {
                var url = "api/dashboards/items/top/problem/items";
                return httpFactory.get(url);
            }

            function getTopProblemItemTypes() {
                var url = "api/dashboards/items/top/problem/itemtypes";
                return httpFactory.get(url);
            }

            function getTopChangingItemTypes() {
                var url = "api/dashboards/items/top/changing/itemtypes";
                return httpFactory.get(url);
            }

            function getPPAPCheckListStatusCounts() {
                var url = "api/dashboards/qualities/ppapchecklist/status/counts";
                return httpFactory.get(url);
            }

            function getSupplierAuditReportCounts() {
                var url = "api/dashboards/qualities/supplierauditreport/counts";
                return httpFactory.get(url);
            }
    
        }
    }
);