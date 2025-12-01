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
                    url: '/project/:projectId',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/project/projectView.jsp',
                    controller: 'ProjectController as projectVm',
                    resolve: ['app/desktop/modules/pm/project/projectController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.home': {
                    url: '/home',
                    templateUrl: 'app/desktop/modules/pm/project/home/projectHomeView.jsp',
                    controller: 'ProjectHomeController as projectHomeVm',
                    resolve: ['app/desktop/modules/pm/project/home/projectHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.home.basic': {
                    url: '/basic',
                    templateUrl: 'app/desktop/modules/pm/project/home/tabs/basic/basicView.jsp',
                    controller: 'BasicController as basicVm',
                    resolve: ['app/desktop/modules/pm/project/home/tabs/basic/basicController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.home.tasks': {
                    url: '/home',
                    templateUrl: 'app/desktop/modules/pm/project/home/tabs/tasks/taskHomeView.jsp',
                    controller: 'TaskHomeController as tasksVm',
                    resolve: ['app/desktop/modules/pm/project/home/tabs/tasks/taskHomeController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.wbs': {
                    url: '/wbs',
                    templateUrl: 'app/desktop/modules/pm/wbs/wbsView.jsp',
                    controller: 'WbsController as wbsVm',
                    resolve: ['app/desktop/modules/pm/wbs/wbsController'],
                    css: cssConfig.getViewCss('wbs')
                },
                'app.pm.project.works': {
                    url: '/works',
                    templateUrl: 'app/desktop/modules/pm/wbs/worksView.jsp',
                    controller: 'WorksController as worksVm',
                    resolve: ['app/desktop/modules/pm/wbs/worksController'],
                    css: cssConfig.getViewCss('wbs')
                },
                'app.pm.project.bom': {
                    url: '/bom',
                    templateUrl: 'app/desktop/modules/pm/bom/bomView.jsp',
                    controller: 'BomController as bomVm',
                    resolve: ['app/desktop/modules/pm/bom/bomController'],
                    css: cssConfig.getViewCss('bom')
                },

                'app.pm.project.projectAdmin': {
                    url: '/projectAdmin',
                    abstract: true,
                    templateUrl: 'app/desktop/modules/pm/projectAdmin/adminMainView.jsp',
                    controller: 'AdminMainController as adminMainVm',
                    resolve: ['app/desktop/modules/pm/projectAdmin/adminMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.projectAdmin.team': {
                    url: '/team',
                    templateUrl: 'app/desktop/modules/pm/projectAdmin/team/teamView.jsp',
                    controller: 'TeamController as teamVm',
                    resolve: ['app/desktop/modules/pm/projectAdmin/team/teamController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.projectAdmin.team.org': {
                    url: '/org',
                    templateUrl: 'app/desktop/modules/pm/projectAdmin/team/all/organizationView.jsp',
                    controller: 'OrganizationController as orgVm',
                    resolve: ['app/desktop/modules/pm/projectAdmin/team/all/organizationController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.projectAdmin.team.role': {
                    url: '/role',
                    templateUrl: 'app/desktop/modules/pm/projectAdmin/role/roleView.jsp',
                    controller: 'RoleController as roleVm',
                    resolve: ['app/desktop/modules/pm/projectAdmin/role/roleController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.projectAdmin.team.allPersons': {
                    url: '/all',
                    templateUrl: 'app/desktop/modules/pm/projectAdmin/team/all/personView.jsp',
                    controller: 'PersonController as personVm',
                    resolve: ['app/desktop/modules/pm/projectAdmin/team/all/personController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.pm.project.projectAdmin.team.personDetails': {
                    url: '/details/:personId',
                    templateUrl: 'app/desktop/modules/pm/projectAdmin/details/personDetailsView.jsp',
                    controller: 'PersonDetailsController as personDetailsVm',
                    resolve: ['app/desktop/modules/pm/projectAdmin/details/personDetailsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);