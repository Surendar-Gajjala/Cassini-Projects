define(
    [
        'app/desktop/desktop.css'
    ],
    function(cssConfig) {
        return {
            routes: {
                'app.admin': {
                    url: '/admin',
                    abstract: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/adminMainView.jsp',
                    controller: 'AdminMainController as adminMainVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/adminMainController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.logins': {
                    url: '/logins',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/loginsView.jsp',
                    controller: 'LoginsController as loginsVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/loginsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.usersettings': {
                    url: '/usersettings',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/userSettingsView.jsp',
                    controller: 'userSettingsController as userVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/userSettingsController',
                        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/usersettings/groupTreeViewDirective'],
                    css: cssConfig.getViewCss('app')
                },

                /*'app.admin.usersettings.logins': {
                    url: '/securityLogins',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/loginsView.jsp',
                    controller: 'SecurityLoginsController as loginVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/loginsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.usersettings.roles': {
                    url: '/roles',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/role/rolesView.jsp',
                    controller: 'RolesController as roleVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/role/rolesController.js'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.usersettings.sessions': {
                    url: '/sessions',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/session/sessionsView.jsp',
                    controller: 'SessionsController as sessionVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/session/sessionsController'],
                    css: cssConfig.getViewCss('app')
                },*/


                'app.admin.logindetails': {
                    url: '/:loginId/details',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/details/loginDetailsView.jsp',
                    controller: 'LoginDetailsController as loginDetailsVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/details/loginDetailsController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.admin.logins.new': {
                    url: '/logins/new',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/new/loginDialogView.jsp',
                    controller: 'loginDialogueController as loginDialogueVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/logins/new/loginDialogController'],
                    css: cssConfig.getViewCss('app')
                },

                'app.admin.groups.new': {
                    url: '/groups/new',
                    templateUrl: 'app/assets/bower_components/cassini-platform/js/modules/admin/app/groups/groupDialogView.jsp',
                    controller: 'GroupDialogController as groupDialogVm',
                    resolve: ['app/assets/bower_components/cassini-platform/js/modules/admin/app/groups/groupDialogController'],
                    css: cssConfig.getViewCss('app')
                },



                'app.admin.security': {
                    url: '/security',
                    abstract: true,
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/securityView.jsp',
                    controller: 'SecurityController as securityVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/securityController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.security.logins': {
                    url: '/securityLogins',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/loginsView.jsp',
                    controller: 'SecurityLoginsController as loginVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/login/loginsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.security.roles': {
                    url: '/roles',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/role/rolesView.jsp',
                    controller: 'RolesController as roleVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/role/rolesController.js'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.security.sessions': {
                    url: '/sessions',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/session/sessionsView.jsp',
                    controller: 'SessionsController as sessionVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/session/sessionsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.session': {
                    url: '/sessions/:sessionId',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/session/sessionDetailsView.jsp',
                    controller: 'SessionDetailsController as sessionDetVm',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/security/session/sessionDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.admin.settings': {
                    url: '/securitySettings',
                    templateUrl: 'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/settings/adminSettingsView.jsp',
                    controller: 'AdminSettingsController',
                    resolve: ['app/assets/bower_components/cassini-platform/app/desktop/modules/admin/app/settings/adminSettingsController'],
                    css: cssConfig.getViewCss('app')
                }


            }
        };
    }
);