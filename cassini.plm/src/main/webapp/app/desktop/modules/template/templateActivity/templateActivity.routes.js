define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.templates.activity': {
                    url: '/templateActivity',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/template/templateActivity/templateActivityMainView.jsp',
                    controller: 'TemplateActivityMainController as templateActivityMainVm',
                    resolve: ['app/desktop/modules/template/templateActivity/templateActivityMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.templates.activity.details': {
                    url: '/details/:activityId',
                    templateUrl: 'app/desktop/modules/template/templateActivity/details/templateActivityDetailsView.jsp',
                    controller: 'TemplateActivityDetailsController as templateActivityDetailsVm',
                    resolve: ['app/desktop/modules/template/templateActivity/details/templateActivityDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);