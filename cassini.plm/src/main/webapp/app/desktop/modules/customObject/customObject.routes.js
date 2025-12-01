define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.customobjects': {
                    url: '/customobjects',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/customObject/customObjectMainView.jsp',
                    controller: 'CustomObjectMainController as customObjectMainVm',
                    resolve: ['app/desktop/modules/customObject/customObjectMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.customobjects.all': {
                    url: '/all/:typeName',
                    templateUrl: 'app/desktop/modules/customObject/all/allCustomObjectsView.jsp',
                    controller: 'CustomObjectsController as allCustomObjectsVm',
                    resolve: ['app/desktop/modules/customObject/all/allCustomObjectsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.customobjects.details': {
                    url: '/:customId?tab',
                    templateUrl: 'app/desktop/modules/customObject/details/customObjectDetailsView.jsp',
                    controller: 'CustomObjectDetailsController as customObjectVm',
                    resolve: ['app/desktop/modules/customObject/details/customObjectDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);