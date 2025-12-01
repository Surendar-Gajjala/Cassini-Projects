define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.routes',
        'app/desktop/modules/main/main.routes',
        'app/desktop/modules/home/home.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.routes'
    ],
    function(loginRouteConfig,
             mainRouteConfig,
             homeRouteConfig,
             adminRouteConfig,
             helpRouteConfig) {

        var defaultRouteConfig = {
            otherwise: '/app/home',
            routes: {

            }
        };

        return  [
            defaultRouteConfig,
            loginRouteConfig,
            mainRouteConfig,
            homeRouteConfig,
            adminRouteConfig,
            helpRouteConfig
        ];
    }
);