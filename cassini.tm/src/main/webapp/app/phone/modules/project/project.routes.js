define(
    [
        'app/phone/phone.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.project': {
                    url: '/project',
                    abstract: true,
                    templateUrl: 'app/phone/modules/project/projectMainView.jsp',
                    controller: 'ProjectMainController as projectMainVm',
                    resolve: ['app/phone/modules/project/projectMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.project.all': {
                    url: '/all',
                    templateUrl: 'app/phone/modules/project/all/allProjectsView.jsp',
                    controller: 'AllProjectsController as allProjectsVm',
                    resolve: ['app/phone/modules/project/all/allProjectsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.project.details': {
                    url: '/details',
                    templateUrl: 'app/phone/modules/project/details/projectDetailsView.jsp',
                    controller: 'ProjectDetailsController as projectDetailsVm',
                    resolve: ['app/phone/modules/project/details/projectDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.project.new': {
                    url: '/new',
                    templateUrl: 'app/phone/modules/project/new/newProjectDialogueView.jsp',
                    controller: 'NewProjectDialogueController newProjectDialogueVm',
                    resolve: ['app/phone/modules/project/new/newProjectDialogueController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);

