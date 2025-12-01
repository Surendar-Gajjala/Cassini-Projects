/**
 * Created by swapna on 1/4/18.
 */

define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.pm': {
                    url: '/pm',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/pmMainView.jsp',
                    controller: 'PmMainController as pmMainVm',
                    resolve: ['app/desktop/modules/pm/pmMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project': {
                    url: '/project',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/project/projectView.jsp',
                    controller: 'ProjectController as projectVm',
                    resolve: ['app/desktop/modules/pm/project/projectController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pm/project/all/allProjectsView.jsp',
                    controller: 'AllProjectsController as allProjectsVm',
                    resolve: ['app/desktop/modules/pm/project/all/allProjectsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.new': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pm/project/new/newProjectDialog.jsp',
                    controller: 'NewProjectDialogController as newProjectVm',
                    resolve: ['app/desktop/modules/pm/project/new/newProjectDialogController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.details': {
                    url: '/details/:projectId?tab',
                    templateUrl: 'app/desktop/modules/pm/project/details/projectDetailsView.jsp',
                    controller: 'ProjectDetailsController as projectDetailsVm',
                    resolve: ['app/desktop/modules/pm/project/details/projectDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.basic': {
                    url: '/basic/:projectId',
                    templateUrl: 'app/desktop/modules/pm/project/details/tabs/basic/projectBasicView.jsp',
                    controller: 'ProjectBasicController as projectBasicVm',
                    resolve: ['app/desktop/modules/pm/project/details/tabs/basic/projectBasicController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.team': {
                    url: '/team',
                    templateUrl: 'app/desktop/modules/pm/project/details/tabs/team/all/teamView1.jsp',
                    controller: 'TeamController as teamVm',
                    resolve: ['app/desktop/modules/pm/project/details/tabs/team/all/teamController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.deliverables': {
                    url: '/team',
                    templateUrl: 'app/desktop/modules/pm/project/details/tabs/deliverables/all/deliverablesView.jsp',
                    controller: 'DeliverablesController as deliverablesVm',
                    resolve: ['app/desktop/modules/pm/project/details/tabs/deliverables/all/deliverablesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.program': {
                    url: '/programs',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/program/programView.jsp',
                    controller: 'ProgramController as programVm',
                    resolve: ['app/desktop/modules/pm/program/programController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.program.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pm/program/all/allProgramsView.jsp',
                    controller: 'AllProgramsController as allProgramsVm',
                    resolve: ['app/desktop/modules/pm/program/all/allProgramsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.program.details': {
                    url: '/details/:programId?tab',
                    templateUrl: 'app/desktop/modules/pm/program/details/programDetailsView.jsp',
                    controller: 'ProgramDetailsController as programDetailsVm',
                    resolve: ['app/desktop/modules/pm/program/details/programDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.programtemplate': {
                    url: '/programtemplates',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/programTemplate/programTemplateMainView.jsp',
                    controller: 'ProgramTemplateMainController as programTemplateMainVm',
                    resolve: ['app/desktop/modules/pm/programTemplate/programTemplateMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.programtemplate.all': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pm/programTemplate/all/allProgramTemplatesView.jsp',
                    controller: 'AllProgramTemplatesController as allProgramTemplatesVm',
                    resolve: ['app/desktop/modules/pm/programTemplate/all/allProgramTemplatesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.programtemplate.details': {
                    url: '/details/:templateId?tab',
                    templateUrl: 'app/desktop/modules/pm/programTemplate/details/programTemplateDetailsView.jsp',
                    controller: 'ProgramTemplateDetailsController as programTemplateDetailsVm',
                    resolve: ['app/desktop/modules/pm/programTemplate/details/programTemplateDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);