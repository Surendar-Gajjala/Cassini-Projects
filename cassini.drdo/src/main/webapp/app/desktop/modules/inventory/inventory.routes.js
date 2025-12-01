define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.inventory': {
                    url: '/inventory',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/inventory/inventoryView.jsp',
                    controller: 'InventoryController as inventoryVm',
                    resolve: ['app/desktop/modules/inventory/inventoryController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.inventory.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/inventory/details/inventoryDetailsView.jsp',
                    controller: 'InventoryDetailsController as inventoryDetailsVm',
                    resolve: ['app/desktop/modules/inventory/details/inventoryDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.inventory.inwardReport': {
                    url: '/inwardReport/:referenceId',
                    templateUrl: 'app/desktop/modules/inventory/reports/inward/inwardReportView.jsp',
                    controller: 'InwardReportController as inwardReportVm',
                    resolve: ['app/desktop/modules/inventory/reports/inward/inwardReportController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.inventory.requestReport': {
                    url: '/requestReport/:referenceId',
                    templateUrl: 'app/desktop/modules/inventory/reports/request/requestReportView.jsp',
                    controller: 'RequestReportController as requestReportVm',
                    resolve: ['app/desktop/modules/inventory/reports/request/requestReportController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.inventory.issueReport': {
                    url: '/issueReport/:referenceId',
                    templateUrl: 'app/desktop/modules/inventory/reports/issue/issueReportView.jsp',
                    controller: 'IssueReportController as issueReportVm',
                    resolve: ['app/desktop/modules/inventory/reports/issue/issueReportController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);