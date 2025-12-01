define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.templates.task': {
                    url: '/templateTask',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/template/templateTask/templateTaskMainView.jsp',
                    controller: 'TemplateTaskMainController as templateTaskMainVm',
                    resolve: ['app/desktop/modules/template/templateTask/templateTaskMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.templates.task.details': {
                    url: '/details/:taskId',
                    templateUrl: 'app/desktop/modules/template/templateTask/details/templateTaskDetailsView.jsp',
                    controller: 'TemplateTaskDetailsController as templateTaskDetailsVm',
                    resolve: ['app/desktop/modules/template/templateTask/details/templateTaskDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);