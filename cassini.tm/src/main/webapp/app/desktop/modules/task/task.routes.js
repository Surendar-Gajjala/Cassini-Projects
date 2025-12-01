define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.task': {
                    url: '/task',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/task/taskMainView.jsp',
                    controller: 'TaskMainController as taskMainVm',
                    resolve: ['app/desktop/modules/task/taskMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.task.all': {
                    url: '/all/:mode',
                    templateUrl: 'app/desktop/modules/task/all/allTasksView.jsp',
                    controller: 'AllTasksController as allTasksVm',
                    resolve: ['app/desktop/modules/task/all/allTasksController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.task.details': {
                    url: '/details/:taskId/:projectId',
                    templateUrl: 'app/desktop/modules/task/details/taskDetailsView.jsp',
                    controller: 'TaskDetailsController as taskDetailsVm',
                    resolve: ['app/desktop/modules/task/details/taskDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.task.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/task/new/newTaskDialogueView.jsp',
                    controller: 'NewTaskDialogueController newTaskDialogueVm',
                    resolve: ['app/desktop/modules/task/new/newTaskDialogueController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.task.print': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/task/print/printDialogueView.jsp',
                    controller: 'PrintDialogueController printDialogueVm',
                    resolve: ['app/desktop/modules/task/print/printDialogueController'],
                    css: cssConfig.getViewCss('app')
                },


            }
        };
    }
);