define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pm.project.sites': {
                    url: '/site',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/site/sitesMainView.jsp',
                    controller: 'SitesMainController as sitesMainVm',
                    resolve: ['app/desktop/modules/site/sitesMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.sites.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/site/all/allSitesView.jsp',
                    controller: 'SitesAllController as sitesVm',
                    resolve: ['app/desktop/modules/site/all/allSitesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.sites.details': {
                    url: '/:siteId',
                    templateUrl: 'app/desktop/modules/site/details/siteDetailsView.jsp',
                    controller: 'SiteDetailsController as siteVm',
                    resolve: ['app/desktop/modules/site/details/siteDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);