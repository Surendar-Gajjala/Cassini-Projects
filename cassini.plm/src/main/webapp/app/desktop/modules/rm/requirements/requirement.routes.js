define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.rm.requirements': {
                    url: '/requirement',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/rm/requirements/requirementMainView.jsp',
                    controller: 'RequirementMainController as reqMainVm',
                    resolve: ['app/desktop/modules/rm/requirements/requirementMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.rm.requirements.details': {
                    url: '/details/:requirementId?tab',
                    templateUrl: 'app/desktop/modules/rm/requirements/details/requirementDetailsView.jsp',
                    controller: 'RequirementDetailsController as requirementDetailsVm',
                    resolve: ['app/desktop/modules/rm/requirements/details/requirementDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);