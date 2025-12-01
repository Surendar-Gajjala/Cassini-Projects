define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.reports': {
                    url: '/reports',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/reports/reportsMainView.jsp',
                    controller: 'ReportMainController as reportMainVm',
                    resolve: ['app/desktop/modules/reports/reportsMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.reports.shortage': {
                    url: '/shortage',
                    templateUrl: 'app/desktop/modules/reports/shortage/shortageReportView.jsp',
                    controller: 'ShortageReportController as shortRepVm',
                    resolve: ['app/desktop/modules/reports/shortage/shortageReportController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);