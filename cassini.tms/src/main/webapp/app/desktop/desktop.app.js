define([
        'app/desktop/desktop.routes',
        'app/desktop/desktop.root',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/common/common.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/dialogs/dialogs.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/login/login.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/admin/admin.module',
        'app/assets/bower_components/cassini-platform/app/desktop/modules/help/help.module',
        'app/desktop/modules/home/home.module',
        'app/desktop/modules/main/main.module'
    ],

    function (routeConfigs) {

        var module = angular.module('app', [
            'app.root',
            'app.admin',
            'app.login',
            'app.common',
            'app.dialogs',
            'app.help',
            'tms.main',
            'tms.home'
        ]);

        module.config(configFunc);

        function configFunc($stateProvider, $urlRouterProvider, $locationProvider,
                            $controllerProvider, $compileProvider, $filterProvider,
                            $provide, $logProvider, $httpProvider, dependencyProvider,
                            uiGmapGoogleMapApiProvider) {

            module.controller = $controllerProvider.register;
            module.directive = $compileProvider.directive;
            module.filter = $filterProvider.register;
            module.factory = $provide.factory;
            module.service = $provide.service;
            module.stateProvider = $stateProvider;

            uiGmapGoogleMapApiProvider.configure({
                key: 'AIzaSyBlHwvmLJy0OZFI6d7C0shCZVe2tg3zu2g',
                v: '3.20', //defaults to latest 3.X anyhow
                libraries: 'weather,geometry,visualization'
            });

            angular.forEach(routeConfigs, function (config) {
                dependencyProvider.configRoutes($urlRouterProvider, $stateProvider, config);
            });

        }

        module.run(
            function (editableOptions, editableThemes) {
                editableOptions.theme = 'bs3';
                editableThemes.bs3.inputClass = 'input-sm';
                editableThemes.bs3.buttonsClass = 'btn-sm';
            }
        );

        return module;
    }
);