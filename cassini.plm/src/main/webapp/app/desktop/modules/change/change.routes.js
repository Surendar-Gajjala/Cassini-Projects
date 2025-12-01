define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.changes': {
                    url: '/changes',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/changeMainView.jsp',
                    controller: 'ChangeMainController as changeMainVm',
                    resolve: ['app/desktop/modules/change/changeMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.eco': {
                    url: '/eco',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/eco/ecoHomeView.jsp',
                    controller: 'ECOHomeController as ecoHomeVm',
                    resolve: ['app/desktop/modules/change/eco/ecoHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.eco.all': {
                    url: '/all?mode?queryData',
                    templateUrl: 'app/desktop/modules/change/eco/all/ecosView.jsp',
                    controller: 'ECOsController as ecosVm',
                    resolve: ['app/desktop/modules/change/eco/all/ecosController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.eco.details': {
                    url: '/:ecoId?tab?workflowActivity',
                    templateUrl: 'app/desktop/modules/change/eco/details/ecoDetailsView.jsp',
                    controller: 'ECODetailsController as ecoVm',
                    resolve: ['app/desktop/modules/change/eco/details/ecoDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.ecr': {
                    url: '/ecr',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/ecr/ecrHomeView.jsp',
                    controller: 'ECRHomeController as ecrHomeVm',
                    resolve: ['app/desktop/modules/change/ecr/ecrHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.ecr.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/change/ecr/all/ecrsView.jsp',
                    controller: 'ECRsController as ecrsVm',
                    resolve: ['app/desktop/modules/change/ecr/all/ecrsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.ecr.details': {
                    url: '/:ecrId?tab',
                    templateUrl: 'app/desktop/modules/change/ecr/details/ecrDetailsView.jsp',
                    controller: 'ECRDetailsController as ecrDetailsVm',
                    resolve: ['app/desktop/modules/change/ecr/details/ecrDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.dcr': {
                    url: '/dcr',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/dcr/dcrHomeView.jsp',
                    controller: 'DCRHomeController as dcrHomeVm',
                    resolve: ['app/desktop/modules/change/dcr/dcrHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.dcr.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/change/dcr/all/dcrsView.jsp',
                    controller: 'DCRsController as dcrsVm',
                    resolve: ['app/desktop/modules/change/dcr/all/dcrsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.dcr.details': {
                    url: '/:dcrId?tab',
                    templateUrl: 'app/desktop/modules/change/dcr/details/dcrDetailsView.jsp',
                    controller: 'DCRDetailsController as dcrVm',
                    resolve: ['app/desktop/modules/change/dcr/details/dcrDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.dco': {
                    url: '/dco',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/dco/dcoHomeView.jsp',
                    controller: 'DCOHomeController as dcoHomeVm',
                    resolve: ['app/desktop/modules/change/dco/dcoHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.dco.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/change/dco/all/dcosView.jsp',
                    controller: 'DCOsController as dcosVm',
                    resolve: ['app/desktop/modules/change/dco/all/dcosController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.dco.details': {
                    url: '/:dcoId?tab',
                    templateUrl: 'app/desktop/modules/change/dco/details/dcoDetailsView.jsp',
                    controller: 'DCODetailsController as dcoVm',
                    resolve: ['app/desktop/modules/change/dco/details/dcoDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.mco': {
                    url: '/mco',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/mco/mcoHomeView.jsp',
                    controller: 'MCOHomeController as mcoHomeVm',
                    resolve: ['app/desktop/modules/change/mco/mcoHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.mco.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/change/mco/all/mcosView.jsp',
                    controller: 'MCOsController as mcosVm',
                    resolve: ['app/desktop/modules/change/mco/all/mcosController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.mco.details': {
                    url: '/:mcoId?tab',
                    templateUrl: 'app/desktop/modules/change/mco/details/mcoDetailsView.jsp',
                    controller: 'MCODetailsController as mcoDetailsVm',
                    resolve: ['app/desktop/modules/change/mco/details/mcoDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.variance': {
                    url: '/variance',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/change/variance/varianceView.jsp',
                    controller: 'VarianceController as varianceVm',
                    resolve: ['app/desktop/modules/change/variance/varianceController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.variance.all': {
                    url: '/all/:varianceMode',
                    templateUrl: 'app/desktop/modules/change/variance/all/allVarianceView.jsp',
                    controller: 'AllVarianceController as allVarianceVm',
                    resolve: ['app/desktop/modules/change/variance/all/allVarianceController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.changes.variance.details': {
                    url: '/:varianceId?tab',
                    templateUrl: 'app/desktop/modules/change/variance/details/varianceDetailsView.jsp',
                    controller: 'VarianceDetailsController as varianceDetailsVm',
                    resolve: ['app/desktop/modules/change/variance/details/varianceDetailsController'],
                    css: cssConfig.getViewCss('app')
                }

            }
        };
    }
);