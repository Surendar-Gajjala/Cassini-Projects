define([
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.routes',
        'app/desktop/modules/main/main.routes',
        'app/desktop/modules/home/home.routes',
    ],

    function (loginRouteConfig,
              adminRouteConfig,
              helpRouteconfig,
              mainRouteConfig,
              homeRouteConfig
    ) {

        var defaultRouteConfig = {
            otherwise: '/app/home',
            routes: {}
        };

        return [
            defaultRouteConfig,
            loginRouteConfig,
            adminRouteConfig,
            helpRouteconfig,
            mainRouteConfig,
            homeRouteConfig
        ];
    }
);