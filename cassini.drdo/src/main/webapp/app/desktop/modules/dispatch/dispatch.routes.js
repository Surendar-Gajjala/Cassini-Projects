define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.dispatch': {
                    url: '/dispatches',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/dispatch/dispatchMainView.jsp',
                    controller: 'DispatchMainController as dispatchMainVm',
                    resolve: ['app/desktop/modules/dispatch/dispatchMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dispatch.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/dispatch/all/allDispatchView.jsp',
                    controller: 'AllDispatchController as allDispatchVm',
                    resolve: ['app/desktop/modules/dispatch/all/allDispatchController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);