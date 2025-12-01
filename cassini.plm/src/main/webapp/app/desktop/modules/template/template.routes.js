/**
 * Created by subramanyam reddy on 15-03-2018.
 */

define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.templates': {
                    url: '/templates',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/template/templateMainView.jsp',
                    controller: 'TemplateMainController as templateMainVm',
                    resolve: ['app/desktop/modules/template/templateMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.templates.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/template/all/allTemplatesView.jsp',
                    controller: 'AllTemplatesController as allTemplatesVm',
                    resolve: ['app/desktop/modules/template/all/allTemplatesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.templates.details': {
                    url: '/details/:templateId?tab',
                    templateUrl: 'app/desktop/modules/template/details/templateDetailsView.jsp',
                    controller: 'TemplateDetailsController as templateDetailsVm',
                    resolve: ['app/desktop/modules/template/details/templateDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);