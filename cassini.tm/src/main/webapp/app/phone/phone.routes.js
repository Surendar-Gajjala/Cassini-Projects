define(
    [
        'app/assets/bower_components/cassini-platform/app/phone/modules/login/login.routes',
        'app/phone/modules/home/home.routes',
        'app/phone/modules/main/main.routes',
        'app/phone/modules/person/person.routes',
        'app/phone/modules/dept/dept.routes',
        'app/phone/modules/task/task.routes',
        'app/phone/modules/accomm/accomm.routes'
    ],
    function (loginRouteConfig,
              homeRouteCongfig,
              mainRouteConfig,
              personRouteConfig,
              deptRouteConfig,
              taskRouteConfig,
              accommRouteConfig) {

        var defaultRouteConfig = {
            otherwise: '/app/home',
            routes: {}
        };

        return [
            defaultRouteConfig,
            loginRouteConfig,
            homeRouteCongfig,
            mainRouteConfig,
            personRouteConfig,
            deptRouteConfig,
            taskRouteConfig,
            accommRouteConfig
        ];
    }
);