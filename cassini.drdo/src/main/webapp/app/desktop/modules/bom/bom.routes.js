define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.bom': {
                    url: '/bom',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/bom/bomMainView.jsp',
                    controller: 'BomMainController as bomMainVm',
                    resolve: ['app/desktop/modules/bom/bomMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.bom.view': {
                    url: '/bom/view',
                    templateUrl: 'app/desktop/modules/bom/bomView.jsp',
                    controller: 'BomController as bomVm',
                    resolve: ['app/desktop/modules/bom/bomController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.bom.partTracking': {
                    url: '/track/:bomId',
                    templateUrl: 'app/desktop/modules/bom/details/partTracking/showPartTrackingView.jsp',
                    controller: 'ShowPartTrackingController as showPartVm',
                    resolve: ['app/desktop/modules/bom/details/partTracking/showPartTrackingController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);