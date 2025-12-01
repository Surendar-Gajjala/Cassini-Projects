/**
 * Created by Suresh Cassini on 03-Jul-18.
 */
define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.routes',
        'app/desktop/modules/main/main.routes',
        'app/desktop/modules/home/home.routes',
        'app/desktop/modules/definition/definition.routes',
        'app/desktop/modules/config/config.routes',
        'app/desktop/modules/run/run.routes',
        'app/desktop/modules/settings/settings.routes'

    ],
    function (loginRouteConfig,
              adminRouteConfig,
              helpRouteConfig,
              mainRouteConfig,
              homeRouteConfig,
              definationRouteConfig,
              configRouteConfig,
              runRouteConfig,
              settingsRouteConfig
    ) {

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
            definationRouteConfig,
            configRouteConfig,
            runRouteConfig,
            settingsRouteConfig
        ];
    }
);