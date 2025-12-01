define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/desktop/modules/main/main.module',
        'app/desktop/modules/home/home.module',
        'app/shared/services/services.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module'
    ],

    function (routeConfigs) {

        var module = angular.module('app', [
                'app.root',
                'app.common',
                'app.dialogs',
                'app.login',
                'app.admin',
                'app.help',
                'mes.main',
                'mes.home',
                'mes.services'

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

            angular.forEach(routeConfigs, function(config) {
                dependencyProvider.configRoutes($urlRouterProvider, $stateProvider, config);
            });

        }

        module.run(
            function(editableOptions, editableThemes) {
                editableOptions.theme = 'bs3'; // bootstrap3 theme. Can be also 'bs2', 'default'
                editableThemes.bs3.inputClass = 'input-sm';
                editableThemes.bs3.buttonsClass = 'btn-sm';
            }
        );
        return module;
    }

);