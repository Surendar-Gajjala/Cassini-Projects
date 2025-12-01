define(
    [
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.routes',
        'app/desktop/modules/main/main.routes',
        'app/desktop/modules/dashboard/home.routes',
        'app/desktop/modules/project/project.routes',
        'app/desktop/modules/task/task.routes',
        'app/desktop/modules/person/person.routes',
        'app/desktop/modules/shift/shift.routes',
        'app/desktop/modules/report/report.routes',
        'app/desktop/modules/accommodation/accommodation.routes',
        'app/desktop/modules/department/department.routes',
        'app/desktop/modules/layout/layout.routes',
        'app/desktop/modules/media/media.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.routes',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.routes'
    ],
    function (loginRouteConfig,
              mainRouteConfig,
              homeRouteConfig,
              projectRouteConfig,
              taskRouteConfig,
              personRouteConfig,
              shiftRouteConfig,
              reportRouteConfig,
              departmentRouteConfig,
              layoutRouteConfig,
              mediaRouteConfig,
              accommodationRouteConfig,
              adminRouteConfig,
              helpRouteConfig) {

        var defaultRouteConfig = {
            otherwise: '/app/home',
            routes: {}
        };

        return [
            defaultRouteConfig,
            loginRouteConfig,
            mainRouteConfig,
            homeRouteConfig,
            projectRouteConfig,
            taskRouteConfig,
            personRouteConfig,
            shiftRouteConfig,
            reportRouteConfig,
            departmentRouteConfig,
            layoutRouteConfig,
            mediaRouteConfig,
            accommodationRouteConfig,
            adminRouteConfig,
            helpRouteConfig
        ];
    }
);