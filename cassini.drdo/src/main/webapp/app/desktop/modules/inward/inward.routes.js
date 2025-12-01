define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.inwards': {
                    url: '/inwards',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/inward/inwardMainView.jsp',
                    controller: 'InwardMainController as inwardMainVm',
                    resolve: ['app/desktop/modules/inward/inwardMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.inwards.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/inward/all/inwardsView.jsp',
                    controller: 'InwardsController as inwardsVm',
                    resolve: ['app/desktop/modules/inward/all/inwardsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.inwards.details': {
                    url: '/details/:inwardId?mode',
                    templateUrl: 'app/desktop/modules/inward/details/inwardDetailsView.jsp',
                    controller: 'InwardDetailsController as inwardDetailsVm',
                    resolve: ['app/desktop/modules/inward/details/inwardDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);