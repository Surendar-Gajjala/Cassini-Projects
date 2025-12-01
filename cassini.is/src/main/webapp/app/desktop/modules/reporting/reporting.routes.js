define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.reporting': {
                    url: '/reporting',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/reporting/reportingMainView.jsp',
                    controller: 'ReportingMainController as reportingMainVm',
                    resolve: ['app/desktop/modules/reporting/reportingMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.reports': {
                    url: '/reports',
                    templateUrl: 'app/desktop/modules/reporting/reports/reportsView.jsp',
                    controller: 'ReportsController as reportsVm',
                    resolve: ['app/desktop/modules/reporting/reports/reportsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);