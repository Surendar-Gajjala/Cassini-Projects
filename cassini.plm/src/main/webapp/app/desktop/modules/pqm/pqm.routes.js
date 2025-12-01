define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pqm': {
                    url: '/pqm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/pqmMainView.jsp',
                    controller: 'PqmMainController as pqmMainVm',
                    resolve: ['app/desktop/modules/pqm/pqmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.inspectionPlan': {
                    url: '/inspectionPlan',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/inspectionPlan/inspectionPlanView.jsp',
                    controller: 'InspectionPlanController as inspectionPlanVm',
                    resolve: ['app/desktop/modules/pqm/inspectionPlan/inspectionPlanController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.inspectionPlan.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/inspectionPlan/all/allInspectionPlansView.jsp',
                    controller: 'AllInspectionPlansController as allInspectionPlansVm',
                    resolve: ['app/desktop/modules/pqm/inspectionPlan/all/allInspectionPlansController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.inspectionPlan.details': {
                    url: '/:planId?tab',
                    templateUrl: 'app/desktop/modules/pqm/inspectionPlan/details/inspectionPlanDetailsView.jsp',
                    controller: 'InspectionPlanDetailsController as inspectionPlanDetailsVm',
                    resolve: ['app/desktop/modules/pqm/inspectionPlan/details/inspectionPlanDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.inspection': {
                    url: '/inspections',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/inspection/inspectionView.jsp',
                    controller: 'InspectionController as inspectionVm',
                    resolve: ['app/desktop/modules/pqm/inspection/inspectionController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.inspection.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/inspection/all/allInspectionsView.jsp',
                    controller: 'AllInspectionsController as allInspectionsVm',
                    resolve: ['app/desktop/modules/pqm/inspection/all/allInspectionsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.inspection.details': {
                    url: '/:inspectionId?tab',
                    templateUrl: 'app/desktop/modules/pqm/inspection/details/inspectionDetailsView.jsp',
                    controller: 'InspectionDetailsController as inspectionDetailsVm',
                    resolve: ['app/desktop/modules/pqm/inspection/details/inspectionDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.ncr': {
                    url: '/ncr',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/ncr/NCRView.jsp',
                    controller: 'NCRController as ncrVm',
                    resolve: ['app/desktop/modules/pqm/ncr/NCRController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.ncr.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/ncr/all/allNCRView.jsp',
                    controller: 'AllNCRController as allNcrVm',
                    resolve: ['app/desktop/modules/pqm/ncr/all/allNCRController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.ncr.details': {
                    url: '/:ncrId?tab',
                    templateUrl: 'app/desktop/modules/pqm/ncr/details/ncrDetailsView.jsp',
                    controller: 'NcrDetailsController as ncrDetailsVm',
                    resolve: ['app/desktop/modules/pqm/ncr/details/ncrDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.qcr': {
                    url: '/qcr',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/qcr/QCRView.jsp',
                    controller: 'QCRController as qcrVm',
                    resolve: ['app/desktop/modules/pqm/qcr/QCRController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.qcr.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/qcr/all/allQCRView.jsp',
                    controller: 'AllQCRController as allQcrVm',
                    resolve: ['app/desktop/modules/pqm/qcr/all/allQCRController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.qcr.details': {
                    url: '/:qcrId?tab',
                    templateUrl: 'app/desktop/modules/pqm/qcr/details/qcrDetailsView.jsp',
                    controller: 'QcrDetailsController as qcrDetailsVm',
                    resolve: ['app/desktop/modules/pqm/qcr/details/qcrDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.pr': {
                    url: '/problemReport',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/problemReport/problemReportView.jsp',
                    controller: 'ProblemReportController as problemReportVm',
                    resolve: ['app/desktop/modules/pqm/problemReport/problemReportController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.pr.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/problemReport/all/allProblemReportView.jsp',
                    controller: 'AllProblemReportController as allProblemReportVm',
                    resolve: ['app/desktop/modules/pqm/problemReport/all/allProblemReportController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.pr.details': {
                    url: '/:problemReportId?tab',
                    templateUrl: 'app/desktop/modules/pqm/problemReport/details/problemReportDetailsView.jsp',
                    controller: 'ProblemReportDetailsController as problemReportDetailsVm',
                    resolve: ['app/desktop/modules/pqm/problemReport/details/problemReportDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.ppap': {
                    url: '/ppap',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/ppap/PPAPView.jsp',
                    controller: 'PPAPController as ppapVm',
                    resolve: ['app/desktop/modules/pqm/ppap/PPAPController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.ppap.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/ppap/all/allPPAPView.jsp',
                    controller: 'AllPPAPController as allPpapVm',
                    resolve: ['app/desktop/modules/pqm/ppap/all/allPPAPController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.ppap.details': {
                    url: '/:ppapId?tab',
                    templateUrl: 'app/desktop/modules/pqm/ppap/details/ppapDetailsView.jsp',
                    controller: 'PPAPDetailsController as ppapDetailsVm',
                    resolve: ['app/desktop/modules/pqm/ppap/details/ppapDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.supplierAudit': {
                    url: '/supplierAudit',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pqm/supplierAudit/supplierAuditMainView.jsp',
                    controller: 'SupplierAuditMainController as supplierAuditMainVm',
                    resolve: ['app/desktop/modules/pqm/supplierAudit/supplierAuditMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.supplierAudit.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pqm/supplierAudit/all/allSupplierAuditView.jsp',
                    controller: 'AllSupplierAuditController as allSupplierAuditVm',
                    resolve: ['app/desktop/modules/pqm/supplierAudit/all/allSupplierAuditController'],

                    css: cssConfig.getViewCss('app')
                },
                'app.pqm.supplierAudit.details': {
                    url: '/:supplierAuditId?tab',
                    templateUrl: 'app/desktop/modules/pqm/supplierAudit/details/supplierAuditDetailsView.jsp',
                    controller: 'SupplierAuditDetailsController as supplierAuditDetailsVm',
                    resolve: ['app/desktop/modules/pqm/supplierAudit/details/supplierAuditDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);