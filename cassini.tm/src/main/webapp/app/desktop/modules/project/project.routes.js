/**
 * Created by Anusha on 11-07-2016.
 */
define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.project': {
                    url: '/project',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/project/projectMainView.jsp',
                    controller: 'ProjectMainController as projectMainVm',
                    resolve: ['app/desktop/modules/project/projectMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.project.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/project/all/allProjectsView.jsp',
                    controller: 'AllProjectsController as allProjectsVm',
                    resolve: ['app/desktop/modules/project/all/allProjectsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.project.details': {
                    url: '/:projectId',
                    templateUrl: 'app/desktop/modules/project/details/projectDetailsView.jsp',
                    controller: 'ProjectDetailsController as projectDetailsVm',
                    resolve: ['app/desktop/modules/project/details/projectDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.project.new': {
                    url: '/new',
                    templateUrl: 'app/desktop/modules/project/new/NewProjectDialogueView.jsp',
                    controller: 'NewProjectDialogueController newProjectDialogueVm',
                    resolve: ['app/desktop/modules/project/new/newProjectDialogueController'],
                    css: cssConfig.getViewCss('app')
                },

            }
        };
    }
);