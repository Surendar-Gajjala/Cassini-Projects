define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/shared/services/services.module',
        'app/desktop/modules/main/main.module',
        'app/desktop/modules/dashboard/home.module',
        'app/desktop/modules/project/project.module',
        'app/desktop/modules/task/task.module',
        'app/desktop/modules/person/person.module',
        'app/desktop/modules/shift/shift.module',
        'app/desktop/modules/report/report.module',
        'app/desktop/modules/accommodation/accommodation.module',
        'app/desktop/modules/department/department.module',
        'app/desktop/modules/layout/layout.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'app/desktop/modules/widgets/tasks/taskWidget.module',
        'app/desktop/modules/widgets/persons/personWidget.module',
        'app/desktop/modules/widgets/reports/reportsWidget.module',
        'app/desktop/modules/widgets/departments/departmentWidget.module',
        'app/desktop/modules/widgets/accommodation/accommodation.module',
        'app/desktop/modules/media/media.module'
    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'tm.services',
                'tm.main',
                'tm.home',
                'tm.project',
                'tm.task',
                'tm.person',
                'tm.shift',
                'tm.report',
                'tm.accommodation',
                'tm.department',
                'tm.layout',
                'tm.media',
                'app.admin',
                'hm.taskWidget',
                'hm.personWidget',
                'hm.reportsWidget',
                'hm.departmentWidget',
                'hm.accommodation',
                'app.help'
            ]
        );

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;

            angular.forEach(routeConfigs, function (config) {
                dependencyProvider.configRoutes($urlRouterProvider, $stateProvider, config);
            });

        }

        module.run(
            function (editableOptions, editableThemes) {
                editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
                editableThemes.bs3.inputClass = 'input-sm';
                editableThemes.bs3.buttonsClass = 'btn-sm';
            }
        );

        return module;
    }
);