define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.mfr.mfrparts': {
                    url: '/mfrparts',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/mfr/mfrparts/mfrpartsMainView.jsp',
                    controller: 'MfrpartsMainController as mfrpartsMainVm',
                    resolve: ['app/desktop/modules/mfr/mfrparts/mfrpartsMainController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.mfr.mfrparts.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/mfr/mfrparts/all/allMfrPartsView.jsp',
                    controller: 'AllMfrPartsController as allMfrPartsVm',
                    resolve: ['app/desktop/modules/mfr/mfrparts/all/allMfrPartsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.mfr.mfrparts.details': {
                    url: '/details/:mfrId/:manufacturePartId?tab',
                    templateUrl: 'app/desktop/modules/mfr/mfrparts/details/mfrpartDetailsView.jsp',
                    controller: 'MfrpartDetailsConroller as mfrpartsDetailsVm',
                    resolve: ['app/desktop/modules/mfr/mfrparts/details/mfrpartDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.mfr.mfrparts.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/mfr/mfrparts/new/newmfrpartView.jsp',
                    controller: 'NewMfrPartController as newmfrpartVm',
                    resolve: ['app/desktop/modules/mfr/mfrparts/new/newmfrpartController'],
                    css: cssConfig.getViewCss('app')
                }

            }
        };
    }
);