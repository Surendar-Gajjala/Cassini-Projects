define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.req.document.template': {
                    url: '/templates',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/reqTemplate/reqDocTemplateMainView.jsp',
                    controller: 'ReqDocTemplateMainController as reqDocTemplateVm',
                    resolve: ['app/desktop/modules/reqTemplate/reqDocTemplateMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.document.template.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/reqTemplate/all/allReqDocTemplateView.jsp',
                    controller: 'ReqDocTemplateController as reqDocTemplateVm',
                    resolve: ['app/desktop/modules/reqTemplate/all/allReqDocTemplateController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.document.template.details': {
                    url: '/details/:reqDocId?tab',
                    templateUrl: 'app/desktop/modules/reqTemplate/details/reqDocTemplateDetailsView.jsp',
                    controller: 'ReqDocTemplateDetailsController as reqDocTemplateDetailsVm',
                    resolve: ['app/desktop/modules/reqTemplate/details/reqDocTemplateDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);