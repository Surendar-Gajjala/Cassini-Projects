define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.report': {
                    url: '/report',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/report/reportMainView.jsp',
                    controller: 'ReportMainController as reportMainVm',
                    resolve: ['app/desktop/modules/report/reportMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.report.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/report/all/allReportsView.jsp',
                    controller: 'AllReportsController as allReportsVm',
                    resolve: ['app/desktop/modules/report/all/allReportsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.report.details': {
                    url: '/details/:reportId',
                    templateUrl: 'app/desktop/modules/report/details/reportDetailsView.jsp',
                    controller: 'ReportDetailsController as reportDetailsVm',
                    resolve: ['app/desktop/modules/report/details/reportDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.report.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/report/new/newReportView.jsp',
                    controller: 'NewReportController newReportVm',
                    resolve: ['app/desktop/modules/report/new/newReportController'],
                    css: cssConfig.getViewCss('app')
                },

            }
        };
    }
);