define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.req.requirements': {
                    url: '/requirement',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/req/reqdocs/requirement/requirementMainView.jsp',
                    controller: 'RequirementMainController as reqMainVm',
                    resolve: ['app/desktop/modules/req/reqdocs/requirement/requirementMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.requirements.details': {
                    url: '/details/:requirementId?tab',
                    templateUrl: 'app/desktop/modules/req/reqdocs/requirement/details/reqDetailsView.jsp',
                    controller: 'ReqDetailsController as reqDetailsVm',
                    resolve: ['app/desktop/modules/req/reqdocs/requirement/details/reqDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);