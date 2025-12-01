define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.nprs': {
                    url: '/nprs',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/npr/nprMainView.jsp',
                    controller: 'NprMainController as nprMainVm',
                    resolve: ['app/desktop/modules/npr/nprMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.nprs.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/npr/all/allNprsView.jsp',
                    controller: 'AllNprsController as allNprsVm',
                    resolve: ['app/desktop/modules/npr/all/allNprsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.nprs.details': {
                    url: '/:nprId?tab',
                    templateUrl: 'app/desktop/modules/npr/details/nprDetailsView.jsp',
                    controller: 'NprDetailsController as nprDetailsVm',
                    resolve: ['app/desktop/modules/npr/details/nprDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);