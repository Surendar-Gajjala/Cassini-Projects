define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pm.project.activity.task': {
                    url: '/task',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/project/task/taskMainView.jsp',
                    controller: 'TaskMainController as taskMainVm',
                    resolve: ['app/desktop/modules/pm/project/task/taskMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.activity.task.details': {
                    url: '/details/:activityId/:taskId?tab',
                    templateUrl: 'app/desktop/modules/pm/project/task/details/taskDetailsView.jsp',
                    controller: 'TaskDetailsController as taskDetailsVm',
                    resolve: ['app/desktop/modules/pm/project/task/details/taskDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);