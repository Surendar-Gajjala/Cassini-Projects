define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.gatePass': {
                    url: '/gatePass',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/gatePass/gatePassMainView.jsp',
                    controller: 'GatePassMainController as gatePassMainVm',
                    resolve: ['app/desktop/modules/gatePass/gatePassMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.gatePass.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/gatePass/all/allGatePassView.jsp',
                    controller: 'AllGatePassController as allGatePassVm',
                    resolve: ['app/desktop/modules/gatePass/all/allGatePassController'],
                    css: cssConfig.getViewCss('app')
                },
            }
        };
    }
);