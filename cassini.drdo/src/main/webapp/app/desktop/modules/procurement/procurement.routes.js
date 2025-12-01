define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.procurement': {
                    url: '/procurement',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/procurement/procurementMainView.jsp',
                    controller: 'ProcurementMainController as procurementMainVm',
                    resolve: ['app/desktop/modules/procurement/procurementMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.procurement.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/procurement/all/procurementView.jsp',
                    controller: 'ProcurementController as procurementVm',
                    resolve: ['app/desktop/modules/procurement/all/procurementController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);