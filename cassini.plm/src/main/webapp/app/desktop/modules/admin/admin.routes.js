define(
    [
        'app/desktop/desktop.css'
    ],
    function (cssConfig) {
        return {
            routes: {
                'app.newadmin': {
                    url: '/newadmin',
                    templateUrl: 'app/desktop/modules/admin/adminView.jsp',
                    controller: 'AdminController as adminVm',
                    resolve: ['app/desktop/modules/admin/adminController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.newadmin.users': {
                    url: '/users',
                    templateUrl: 'app/desktop/modules/admin/user/all/allUsersView.jsp',
                    controller: 'AllUsersController as allUsersVm',
                    resolve: ['app/desktop/modules/admin/user/all/allUsersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.userdetails': {
                    url: '/userdetails/:userId',
                    templateUrl: 'app/desktop/modules/admin/user/details/userDetailsView.jsp',
                    controller: 'UserDetailsController as userDetailsVm',
                    resolve: ['app/desktop/modules/admin/user/details/userDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.userdetails.activity': {
                    url: '/activity',
                    templateUrl: 'app/desktop/modules/admin/user/details/activity/userActivityView.jsp',
                    controller: 'UserActivityController as userActivityVm',
                    resolve: ['app/desktop/modules/admin/user/details/activity/userActivityController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.userdetails.account': {
                    url: '/account',
                    templateUrl: 'app/desktop/modules/admin/user/details/account/userAccountView.jsp',
                    controller: 'UserAccountController as userAccountVm',
                    resolve: ['app/desktop/modules/admin/user/details/account/userAccountController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.userdetails.password': {
                    url: '/password',
                    templateUrl: 'app/desktop/modules/admin/user/details/password/changePasswordView.jsp',
                    controller: 'ChangePasswordController as changePasswordVm',
                    resolve: ['app/desktop/modules/admin/user/details/password/changePasswordController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.userdetails.roles': {
                    url: '/roles',
                    templateUrl: 'app/desktop/modules/admin/user/details/roles/userRolesView.jsp',
                    controller: 'UserRolesController as userRolesVm',
                    resolve: ['app/desktop/modules/admin/user/details/roles/userRolesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.userdetails.settings': {
                    url: '/settings',
                    templateUrl: 'app/desktop/modules/admin/user/details/settings/userSettingsView.jsp',
                    controller: 'UserSettingsController as userSettingsVm',
                    resolve: ['app/desktop/modules/admin/user/details/settings/userSettingsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.newadmin.roles': {
                    url: '/roles',
                    templateUrl: 'app/desktop/modules/admin/role/all/allRolesView.jsp',
                    controller: 'AllRolesController as allRolesVm',
                    resolve: ['app/desktop/modules/admin/role/all/allRolesController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.newadmin.permissions': {
                    url: '/permissions',
                    templateUrl: 'app/desktop/modules/admin/permission/all/allPermissionsView.jsp',
                    controller: 'AllPermissionsController as allPermissionsVm',
                    resolve: ['app/desktop/modules/admin/permission/all/allPermissionsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.roledetails': {
                    url: '/roledetails/:roleId',
                    templateUrl: 'app/desktop/modules/admin/role/details/roleDetailsView.jsp',
                    controller: 'RoleDetailsController as roleDetailsVm',
                    resolve: ['app/desktop/modules/admin/role/details/roleDetailsController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.roledetails.basic': {
                    url: '/basic',
                    templateUrl: 'app/desktop/modules/admin/role/details/basic/roleBasicView.jsp',
                    controller: 'RoleBasicController as roleBasicVm',
                    resolve: ['app/desktop/modules/admin/role/details/basic/roleBasicController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.roledetails.users': {
                    url: '/users',
                    templateUrl: 'app/desktop/modules/admin/role/details/users/roleUsersView.jsp',
                    controller: 'RoleUsersController as roleUsersVm',
                    resolve: ['app/desktop/modules/admin/role/details/users/roleUsersController'],
                    css: cssConfig.getViewCss('app')
                },
                'app.roledetails.permissions': {
                    url: '/permissions',
                    templateUrl: 'app/desktop/modules/admin/role/details/permissions/rolePermissionsView.jsp',
                    controller: 'RolePermissionsController as rolePermissionsVm',
                    resolve: ['app/desktop/modules/admin/role/details/permissions/rolePermissionsController'],
                    css: cssConfig.getViewCss('app')
                }
            }
        };
    }
);