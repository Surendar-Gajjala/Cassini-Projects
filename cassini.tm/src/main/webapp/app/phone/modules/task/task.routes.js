define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.task': {
                    url: '/task',
                    abstract: true,
                    templateUrl: 'app/phone/modules/task/taskMainView.jsp',
                    controller: 'TaskMainController as taskMainVm',
                    resolve: ['app/phone/modules/task/taskMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.task.all': {
                    url: '/all',
                    templateUrl: 'app/phone/modules/task/all/allTasksView.jsp',
                    controller: 'AllTasksController as allTasksVm',
                    resolve: ['app/phone/modules/task/all/allTasksController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.task.details': {
                    url: '/details/:taskId/:projectId',
                    templateUrl: 'app/phone/modules/task/details/taskDetailsView.jsp',
                    controller: 'TaskDetailsController as taskDetailsVm',
                    resolve: ['app/phone/modules/task/details/taskDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.task.new': {
                    url: '/new',
                    templateUrl: 'app/phone/modules/task/new/newTaskView.jsp',
                    controller: 'NewTaskController as newTaskVm',
                    resolve: ['app/phone/modules/task/new/newTaskController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);