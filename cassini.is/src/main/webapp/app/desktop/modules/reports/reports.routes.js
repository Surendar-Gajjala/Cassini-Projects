define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.reports': {
                    url: '/all',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/reports/reportsMainView.jsp',
                    controller: 'ReportMainController as reportMainVm',
                    resolve: ['app/desktop/modules/reports/reportsMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.project': {
                    url: '/project',
                    templateUrl: 'app/desktop/modules/reports/project/projectMaterialStatementView.jsp',
                    controller: 'ProjectMaterialStatementController as projRepVm',
                    resolve: ['app/desktop/modules/reports/project/projectMaterialStatementController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.work': {
                    url: '/work',
                    templateUrl: 'app/desktop/modules/reports/work/workStatusView.jsp',
                    controller: 'WorkStatusController as workRepVm',
                    resolve: ['app/desktop/modules/reports/work/workStatusController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.store': {
                    url: '/store',
                    templateUrl: 'app/desktop/modules/reports/store/storePositionView.jsp',
                    controller: 'StorePositionController as storeRepVm',
                    resolve: ['app/desktop/modules/reports/store/storePositionController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.stock': {
                    url: '/stock',
                    templateUrl: 'app/desktop/modules/reports/stock/stockPositionView.jsp',
                    controller: 'StockPositionController as stockRepVm',
                    resolve: ['app/desktop/modules/reports/stock/stockPositionController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.receive': {
                    url: '/stock/receive',
                    templateUrl: 'app/desktop/modules/reports/stockReceive/stockReceiveItemsReportView.jsp',
                    controller: 'StockReceiveItemsController as stockReceiveVm',
                    resolve: ['app/desktop/modules/reports/stockReceive/stockReceiveItemsReportController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.issue': {
                    url: '/stock/issue',
                    templateUrl: 'app/desktop/modules/reports/stockIssue/stockIssueItemsReportView.jsp',
                    controller: 'StockIssueItemsController as stockIssueVm',
                    resolve: ['app/desktop/modules/reports/stockIssue/stockIssueItemsReportController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);