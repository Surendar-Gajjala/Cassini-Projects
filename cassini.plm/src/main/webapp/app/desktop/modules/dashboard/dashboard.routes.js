define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.dashboard': {
                    url: '/dashboards',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/dashboard/dashboardView.jsp',
                    controller: 'DashboardController as dashboardVm',
                    resolve: ['app/desktop/modules/dashboard/dashboardController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dashboard.quality': {
                    url: '/quality',
                    templateUrl: 'app/desktop/modules/dashboard/quality/qualityDashboardView.jsp',
                    controller: 'QualityDashboardController as qualityDashboardVm',
                    resolve: ['app/desktop/modules/dashboard/quality/qualityDashboardController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dashboard.changes': {
                    url: '/changes',
                    templateUrl: 'app/desktop/modules/dashboard/changes/changeDashboardView.jsp',
                    controller: 'ChangeDashboardController as changeDashboardVm',
                    resolve: ['app/desktop/modules/dashboard/changes/changeDashboardController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dashboard.items': {
                    url: '/items',
                    templateUrl: 'app/desktop/modules/dashboard/item/itemDashboardView.jsp',
                    controller: 'ItemDashboardController as itemDashboardVm',
                    resolve: ['app/desktop/modules/dashboard/item/itemDashboardController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dashboard.oems': {
                    url: '/oems',
                    templateUrl: 'app/desktop/modules/dashboard/oem/oemDashboardView.jsp',
                    controller: 'OEMDashboardController as oemDashboardVm',
                    resolve: ['app/desktop/modules/dashboard/oem/oemDashboardController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dashboard.workflows': {
                    url: '/workflows',
                    templateUrl: 'app/desktop/modules/dashboard/workflows/workflowsDashboardView.jsp',
                    controller: 'WorkflowDashboardController as workflowDashboardVm',
                    resolve: ['app/desktop/modules/dashboard/workflows/workflowsDashboardController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dashboard.projects': {
                    url: '/projects',
                    templateUrl: 'app/desktop/modules/dashboard/project/projectDashboardView.jsp',
                    controller: 'ProjectDashboardController as projectDashboardVm',
                    resolve: ['app/desktop/modules/dashboard/project/projectDashboardController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);