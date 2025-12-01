define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.routes',
        'app/desktop/modules/main/main.routes',
        'app/desktop/modules/home/home.routes',
        'app/desktop/modules/dashboard/dashboard.routes',
        'app/desktop/modules/complaint/complaint.routes',
        'app/desktop/modules/users/users.routes',
        'app/desktop/modules/settings/settings.routes',
        'app/desktop/modules/irsteLogin/irsteLogin.routes'
    ],
    function (loginRouteConfig,
              adminRouteConfig,
              helpRouteConfig,
              mainRouteConfig,
              homeRouteConfig,
              dashboardConfig,
              complaintConfig,
              respondersConfig,
              settingsRouteConfig,
              irsteLoginConfig) {

        var defaultRouteConfig = {
            otherwise: '/app/home',
            routes: {}
        };

        return [
            defaultRouteConfig,
            loginRouteConfig,
            adminRouteConfig,
            helpRouteConfig,
            mainRouteConfig,
            homeRouteConfig,
            dashboardConfig,
            complaintConfig,
            respondersConfig,
            settingsRouteConfig,
            irsteLoginConfig
        ];
    }
);