define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.tm': {
                    url: '/tm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/tm/tmMainView.jsp',
                    controller: 'TmMainController as tmMainVm',
                    resolve: ['app/desktop/modules/tm/tmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.tasks': {
                    url: '/tasks',
                    templateUrl: 'app/desktop/modules/tm/all/tasksView.jsp',
                    controller: 'TasksController as tasksVm',
                    resolve: ['app/desktop/modules/tm/all/tasksController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.newtask': {
                    url: '/tasks/new',
                    templateUrl: 'app/desktop/modules/tm/new/newTaskView.jsp',
                    controller: 'NewTaskController as newTaskVm',
                    resolve: ['app/desktop/modules/tm/new/newTaskController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.taskdetails': {
                    url: '/tasks/:taskId/details',
                    templateUrl: 'app/desktop/modules/tm/details/taskDetailsView.jsp',
                    controller: 'TaskDetailsController as taskDetailsVm',
                    resolve: ['app/desktop/modules/tm/details/taskDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);