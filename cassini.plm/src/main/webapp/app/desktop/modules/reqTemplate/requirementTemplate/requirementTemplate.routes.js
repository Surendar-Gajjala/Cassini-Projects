define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.req.template': {
                    url: '/template',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/reqTemplate/requirementTemplate/requirementTemplateMainView.jsp',
                    controller: 'RequirementTemplateMainController as reqMainVm',
                    resolve: ['app/desktop/modules/reqTemplate/requirementTemplate/requirementTemplateMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.req.template.details': {
                    url: '/details/:requirementId?tab',
                    templateUrl: 'app/desktop/modules/reqTemplate/requirementTemplate/details/reqTemplateDetailsView.jsp',
                    controller: 'ReqTemplateDetailsController as reqTemplateDetailsVm',
                    resolve: ['app/desktop/modules/reqTemplate/requirementTemplate//details/reqTemplateDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);