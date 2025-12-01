define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.dm': {
                    url: '/dm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/dm/dmMainView.jsp',
                    controller: 'DMMainController as dmMainVm',
                    resolve: ['app/desktop/modules/dm/dmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.dm.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/dm/all/allDocumentManagementView.jsp',
                    controller: 'AllDocumentManagementController as allDocumentManagementVm',
                    resolve: ['app/desktop/modules/dm/all/allDocumentManagementController'],
                    css: cssConfig.getViewCss('app')
                },
            }
        };
    }
);