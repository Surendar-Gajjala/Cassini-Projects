define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.req': {
                    url: '/req',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/req/reqMainView.jsp',
                    controller: 'ReqMainController as reqMainVm',
                    resolve: ['app/desktop/modules/req/reqMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.document': {
                    url: '/document',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/req/reqdocs/reqDocumentsHomeView.jsp',
                    controller: 'ReqDocumentsHomeController as reqDocumentsHomeVm',
                    resolve: ['app/desktop/modules/req/reqdocs/reqDocumentsHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.document.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/req/reqdocs/all/reqDocumentsView.jsp',
                    controller: 'ReqDocumentsController as reqDocsVm',
                    resolve: ['app/desktop/modules/req/reqdocs/all/reqDocumentsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.document.details': {
                    url: '/revision/:reqId?tab',
                    templateUrl: 'app/desktop/modules/req/reqdocs/details/reqDocumentDetailsView.jsp',
                    controller: 'ReqDocumentDetailsController as reqDocDetailsVm',
                    resolve: ['app/desktop/modules/req/reqdocs/details/reqDocumentDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);